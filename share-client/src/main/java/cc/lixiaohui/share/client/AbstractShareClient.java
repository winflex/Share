package cc.lixiaohui.share.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.client.handler.ShareMessageHandler;
import cc.lixiaohui.share.protocol.codec.MessageDecoder;
import cc.lixiaohui.share.protocol.codec.MessageEncoder;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;
import cc.lixiaohui.share.util.lifecycle.LifeCycleState;


/**
 * 抽象实现, 实现连接建立, 断开重连等
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午3:59:56
 */
public abstract class AbstractShareClient extends AbstractLifeCycle implements IShareClient {

	protected IConfiguration config;
	
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
	
	protected Collection<IMessageListener> messageListeners = new ArrayList<IMessageListener>();
	
	protected ISerializeFactory serializeFactory;
	
	protected static final String HN_IDLE_STATE = "IdleStateHandler";
	protected static final String HN_DECODER = "ShareMessageDecoder";
	protected static final String HN_ENCODER = "ShareMessageEncoder";
	protected static final String HN_REQUEST = "RequestHandler";
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractShareClient.class);
	
	protected AbstractShareClient(IConfiguration config) {
		this.config = config;
	}
	
	@Override
	protected void initInternal() throws LifeCycleException {
		super.initInternal();
		serializeFactory = createSerializeFactory();
		initGroup();
		initBootstrap();
	}

	@SuppressWarnings("unchecked")
	private ISerializeFactory createSerializeFactory() throws LifeCycleException {
		try {
			Class<ISerializeFactory> clazz = (Class<ISerializeFactory>) Class.forName(config.serializeFactoryClass());
			return clazz.newInstance();
		} catch (Exception e) {
			logger.error("unable to instantiate SerializeFactory by class name {}, cause: {}", config.serializeFactoryClass(), e.getMessage());
			throw new LifeCycleException(e);
		}
	}

	private void initGroup() {
		logger.debug("Initializing Group");
		workerGroup = new NioEventLoopGroup(config.ioThreads());
		logger.debug("Group Initialized");
	}

	private void initBootstrap() {
		logger.debug("Initializing Bootstrap");
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pl = ch.pipeline();
				// 心跳
				pl.addLast(HN_IDLE_STATE, new IdleStateHandler(config.heartbeatInterval(), config.heartbeatInterval(), 0, TimeUnit.MILLISECONDS));
				
				pl.addLast(HN_DECODER, new MessageDecoder(serializeFactory));
				pl.addLast(HN_ENCODER, new MessageEncoder(serializeFactory));
				
				pl.addLast(HN_REQUEST, new ShareMessageHandler(AbstractShareClient.this));
			}
			
		});
		logger.debug("Bootstrap Initialized");
	}
	
	@Override
	protected void startInternal() throws LifeCycleException {
		try {
			doConnect();
			logger.info("Successfully connected to {}:{}", config.host(), config.port());
		} catch (Exception e) {
			logger.error("{}", e);
			throw new LifeCycleException(e);
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
	}

	protected synchronized void doConnect() throws Exception{
		if (connected == true) {
			return;
		}
		
		channel = bootstrap.connect(config.host(), config.port()).sync().channel();
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
		
		logger.info("trying to reconnect to {}:{}", config.host(), config.port());
		
		if (config.reconnectTimes() > 0) { // 有限重连
			for (int i = 0; i < config.reconnectTimes(); i++) {
				try {
					logger.info("reconnecting...");
					doReconnect0();
					logger.info("Successfully reconnected to {}:{} at {}th times trying", config.host(), config.port(), i + 1);
					return;
				} catch (Exception e) {
					logger.info("Unable to reconnect to {}:{} at {}th times trying, cause:{}", config.host(), config.port(), i + 1, e.getMessage());
				}
				// 间隔
				Thread.sleep(config.reconnectInterval());
			}
		} else { // 无限重连
			long times = 0;
			while (true) {
				++times;
				try {
					logger.info("reconnecting...");
					doReconnect0();
					logger.info("Successfully reconnected to {}:{} at {}th times trying", config.host(), config.port(), times);
					return;
				} catch (Exception e) {
					logger.info("Unable to reconnect to {}:{} at {}th times trying, cause:{}", config.host(), config.port(), times, e.getMessage());
				}
				// 间隔
				Thread.sleep(config.reconnectInterval());
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
	
}
