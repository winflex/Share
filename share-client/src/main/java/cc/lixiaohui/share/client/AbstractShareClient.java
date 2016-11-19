package cc.lixiaohui.share.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.client.handler.HeartbeatHandler;
import cc.lixiaohui.share.client.handler.IMessageHandler;
import cc.lixiaohui.share.client.handler.MessageDispatcher;
import cc.lixiaohui.share.client.util.ClientException;
import cc.lixiaohui.share.client.util.ResponseFuture;
import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSCResponseMessage;
import cc.lixiaohui.share.protocol.CSResponseMessage;
import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.HandshakeResponseMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.protocol.codec.MessageDecoder;
import cc.lixiaohui.share.protocol.codec.MessageEncoder;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.NamedThreadFactory;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.future.AbstractFuture;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;
import cc.lixiaohui.share.util.lifecycle.LifeCycleState;


/**
 * 抽象实现, 实现连接建立, 断开重连等
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午3:59:56
 */
public abstract class AbstractShareClient extends AbstractLifeCycle implements IShareClient, IMessageHandler {

	protected String host;
	
	protected int port;
	
	/**
	 * 是否连接到服务端
	 */
	protected volatile boolean connected; 
	
	/**
	 * 若该字段为false, 则每当发现连接断开时, 都会尝试重连 {@link IConfiguration#reconnectTimes()} 
	 * 或  {@link IConfiguration#retryTimesOnStartUpFailed()} 次
	 */
	protected volatile boolean destroyed;
	
	protected Channel channel;
	protected Bootstrap bootstrap;
	protected NioEventLoopGroup workerGroup;
	
	protected volatile boolean handshaked;
	protected HandShakeRequestMessage handshakeMessage;
	
	/**
	 * 消息监听器集
	 */
	protected Collection<IMessageListener> messageListeners = new ArrayList<IMessageListener>();
	
	/**
	 * 已发送但还未接收到响应的消息
	 * messageId -> MessageCache
	 */
	private final Map<Long, ResponseFuture> futures = new ConcurrentHashMap<Long, ResponseFuture>();
	
	private Collection<IConnectionListener> connectionListeners = new ArrayList<IConnectionListener>();
	
	protected ISerializeFactory serializeFactory;
	
	protected ExecutorService executor;
	
	protected static final String HN_IDLE_STATE = "IdleStateHandler";
	protected static final String HN_DECODER = "ShareMessageDecoder";
	protected static final String HN_ENCODER = "ShareMessageEncoder";
	protected static final String HN_REQUEST = "RequestHandler";
	protected static final String HN_HEARTBEAT = "Heartbeat";

	private final HandShakeFuture handShakeFuture = new HandShakeFuture();
	
	private static final String DEFAULT_SERIALIZE_FACTORY = "cc.lixiaohui.share.protocol.codec.serialize.factory.HessianSerializeFactory";
	protected static final Logger logger = LoggerFactory.getLogger(AbstractShareClient.class);
	
	protected AbstractShareClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	protected void initInternal() throws LifeCycleException {
		super.initInternal();
		
		executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("Client-Worker"));
		
		serializeFactory = createSerializeFactory();
		initGroup();
		initBootstrap();
	}

	@SuppressWarnings("unchecked")
	private ISerializeFactory createSerializeFactory() throws LifeCycleException {
		try {
			Class<ISerializeFactory> clazz = (Class<ISerializeFactory>) Class.forName(DEFAULT_SERIALIZE_FACTORY);
			return clazz.newInstance();
		} catch (Exception e) {
			logger.error("unable to instantiate SerializeFactory by class name {}, cause: {}", DEFAULT_SERIALIZE_FACTORY, e.getMessage());
			throw new LifeCycleException(e);
		}
	}

	private void initGroup() {
		logger.debug("Initializing Group");
		workerGroup = new NioEventLoopGroup(1);
		logger.debug("Group Initialized");
	}

	private void initBootstrap() {
		logger.debug("Initializing Bootstrap");
		bootstrap = new Bootstrap();
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pl = ch.pipeline();
				// 心跳
				//pl.addLast(HN_IDLE_STATE, new IdleStateHandler(config.heartbeatInterval(), config.heartbeatInterval(), 0, TimeUnit.MILLISECONDS));
				
				pl.addLast(HN_DECODER, new MessageDecoder(serializeFactory));
				pl.addLast(HN_ENCODER, new MessageEncoder(serializeFactory));
				
				pl.addLast(HN_REQUEST, new MessageDispatcher(AbstractShareClient.this, executor));
			}
			
		});
		logger.debug("Bootstrap Initialized");
	}
	
	@Override
	protected void startInternal() throws LifeCycleException {
		try {
			doConnect();
			logger.info("Successfully connected to {}:{}", host, port);
			fireConnectionConnected();
			
		} catch (Exception e) {
			logger.error("{}", e);
			throw new LifeCycleException("无法连接服务器", e);
		}
		
		// 连接已建立
		channel.closeFuture().addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				logger.warn("Channel {} closed", future.channel());
				connected = false;
				if (!destroyed) { // 被迫断开连接的尝试重连
					tryReconnect();
				}
			}
			
		});
		
		// wait for handshake to complete for 10 seconds
		try {
			handShakeFuture.await(10 * 1000);
		} catch (InterruptedException e) {
			logger.error("{}", e);
			throw new LifeCycleException(e);
		}
		// 到这里说明握手已完成
	}

	protected synchronized void doConnect() throws Exception{
		if (connected == true) {
			return;
		}
		
		channel = bootstrap.connect(host, port).sync().channel();
		connected = true;
		
	}
	
	/**
	 * 尝试重连
	 * 当生命周期处于LifeCycleState.DESTROYING, LifeCycleState.DESTROYED时不重连
	 * @throws InterruptedException
	 */
	protected synchronized void tryReconnect() throws InterruptedException {
		if (connected == true) {
			return ;
		}
		// 当生命周期处于LifeCycleState.DESTROYING, LifeCycleState.DESTROYED时不重连
		if (state == LifeCycleState.DESTROYING || state == LifeCycleState.DESTROYED) {
			logger.info("Client is in LifeCycleState.DESTROYING or LifeCycleState.DESTROYED state, skip reconnection");
			return;
		}
		
		logger.info("trying to reconnect to {}:{}", host, port);
		
		if (handshakeMessage.getReconnectTimes() > 0) { // 有限重连
			for (int i = 0; i < handshakeMessage.getReconnectTimes(); i++) {
				try {
					logger.info("reconnecting...");
					doReconnect0();
					logger.info("Successfully reconnected to {}:{} at {}th times trying", host, port, i + 1);
					return;
				} catch (Exception e) {
					logger.info("Unable to reconnect to {}:{} at {}th times trying, cause:{}", host, port, i + 1, e.getMessage());
				}
				// 间隔
				Thread.sleep(handshakeMessage.getReconnectInterval());
			}
		} else { // 无限重连
			long times = 0;
			while (true) {
				++times;
				try {
					logger.info("reconnecting...");
					doReconnect0();
					logger.info("Successfully reconnected to {}:{} at {}th times trying", host, port, times);
					return;
				} catch (Exception e) {
					logger.info("Unable to reconnect to {}:{} at {}th times trying, cause:{}", host, port, times, e.getMessage());
				}
				// 间隔
				Thread.sleep(handshakeMessage.getReconnectInterval());
			}
		}
	}

	private void doReconnect0() throws Exception {
		initGroup();
		initBootstrap();
		doConnect();
	}
	
	@Override
	protected void destroyInternal() throws LifeCycleException {
		// in LifeCycleState.DESTROYING state
		try {
			// 同步等待连接关闭
			channel.close().sync();
		} catch (InterruptedException e) {
			logger.error("{}", e);
			throw new LifeCycleException(e);
		}
		logger.info("client destroyed");
	}
	
	@Override
	public void handleCSCRequest(ChannelHandlerContext ctx, CSCRequestMessage message) {
		// TODO 构造响应
		CSCResponseMessage resp = CSCResponseMessage.builder().correlationId(message.getId())
			.responseTime(TimeUtils.currentTimeMillis())
			.responseJson(JSONUtils.newSuccessfulResult("发送成功")).build();
		// write response
		writeMessage(ctx, resp);
		// notify listeners
		for (IMessageListener l : messageListeners) {
			l.onChat(message.getFromUserId(), message.getText(), message.getRequestTime());
		}
	}
	
	@Override
	public void handleCSCResponse(ChannelHandlerContext ctx, CSCResponseMessage message) {
		ResponseFuture future = futures.get(message.getCorrelationId());
		if (future == null) { 
			// 没有future关联:1.响应超时了; 2.重复响应; 3.根本就没有请求与该响应对应
			logger.info("no future correlate to message {}", message);
			return;
		}
		future.responsed(message);
		logger.debug("future responsed with message {}", message);
	}
	
	@Override
	public void handleCSResponse(ChannelHandlerContext ctx, CSResponseMessage message) {
		// TODO
		ResponseFuture future = futures.get(message.getCorrelationId());
		if (future == null) { 
			// 没有future关联:1.响应超时了; 2.重复响应; 3.根本就没有请求与该响应对应
			logger.info("no future correlate to message {}", message);
			return;
		}
		future.responsed(message);
		logger.debug("future responsed with message {}", message);
	}
	
	@Override
	public void handleHandshake(final ChannelHandlerContext ctx, HandShakeRequestMessage message) {
		this.handshakeMessage = message;
		this.handshaked = true;
		// 回复响应
		Message resp = HandshakeResponseMessage.builder().build();
		writeMessage(ctx.channel(), resp, new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				long n = handshakeMessage.getHeartbeatInterval();
				if (n > 0) { // 是否开启心跳机制
					future.channel().pipeline().addFirst(HN_IDLE_STATE, new IdleStateHandler(n, 0, 0, TimeUnit.MILLISECONDS));
					future.channel().pipeline().addLast(HN_HEARTBEAT, new HeartbeatHandler());
				}
				logger.info("write handshake response, started to send heartbeat packet");
				logger.info("connection established {}", future.channel());
				// TODO 连接真正建立
				
				handShakeFuture.setHandShaked(true);
				
				fireConnectionConnected();
			}
			
		});
		
	}
	
	@Override
	public void handlePushMessage(ChannelHandlerContext ctx, PushMessage message) {
		fireOnMessagePushed(message);
		// TODO 推送消息需要响应???
	}
	
	
	@Override
	public void addMessageListener(IMessageListener listener) {
		messageListeners.add(listener);
	}

	@Override
	public void addMessageListeners(Collection<IMessageListener> listeners) {
		messageListeners.addAll(listeners);
	}

	@Override
	public void removeMessageListener(IMessageListener listener) {
		messageListeners.remove(listener);
	}
	
	@Override
	public void addConnectionListener(IConnectionListener l) {
		this.connectionListeners.add(l);
	}
	
	@Override
	public void addConnectionListeners(Collection<IConnectionListener> listeners) {
		this.connectionListeners.addAll(listeners);
	}
	
	@Override
	public void removeConnectionListener(IConnectionListener l) {
		this.connectionListeners.remove(l);
	}
	
	protected void putFuture(long id, ResponseFuture future) {
		futures.put(id, future);
	}
	
	protected ResponseFuture removeFuture(long id) {
		return futures.remove(id);
	}
	
	protected void checkHandshake() throws ClientException{
		if (!handshaked) {
			throw new ClientException("握手未能完成");
		}
	}
	
	/**
	 * 往Channel中写Message
	 */
	protected void writeMessage(Channel ch, final Message message) {
		ch.writeAndFlush(message).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				logger.info("write message {}", message);
			}
			
		});
	}
	
	protected void writeMessage(Channel ch, Message message, ChannelFutureListener l) {
		ch.writeAndFlush(message).addListener(l);
	}
	
	protected void writeMessage(ChannelHandlerContext ctx, final Message message) {
		writeMessage(ctx.channel(), message);
	}
	
	protected void fireConnontionClosed(final Throwable cause) {
		executor.execute(new Runnable() {
			 
			@Override
			public void run() {
				for (IConnectionListener l : connectionListeners) {
					l.onClosed(cause);
				}
			}
		});
		
	}
	
	protected void fireConnectionConnected() {
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				for (IConnectionListener l : connectionListeners) {
					l.onConnected();
				}
			}
		});
	
	}
	
	
	protected void fireOnMessagePushed(final PushMessage message) {
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				for (IMessageListener l : messageListeners) {
					switch (message.getType()) {
					case COMMENT:
						l.onComment(message.getPushData());
						break;
					case FRI_REQ:
						l.onFriendRequest(message.getPushData());
						break;
					case FRI_DEL:
						l.onFriendDeleted(message.getPushData());
						break;
					case PRAISE:
						l.onPraise(message.getPushData());
						break;
					case SHARE:
						l.onShare(message.getPushData());
						break;
					default:
						break;
					}
				}
			}
		});
		
	}
	
	
	private class HandShakeFuture extends AbstractFuture<Boolean> {
		
		public void setHandShaked(boolean handshaked) {
			setSuccess(handshaked);
		}
		
		@SuppressWarnings("unused")
		public void failed(Throwable t) {
			setFailure(t);
		}
	}
}
