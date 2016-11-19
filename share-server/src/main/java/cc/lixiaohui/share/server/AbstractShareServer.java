package cc.lixiaohui.share.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.core.config.ServerConfig;
import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSCResponseMessage;
import cc.lixiaohui.share.protocol.CSRequestMessage;
import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.protocol.codec.MessageDecoder;
import cc.lixiaohui.share.protocol.codec.MessageEncoder;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;
import cc.lixiaohui.share.server.handler.AuthFilter;
import cc.lixiaohui.share.server.handler.HandshakeHandler;
import cc.lixiaohui.share.server.handler.HeartbeatHandler;
import cc.lixiaohui.share.server.handler.MessageDispatcher;
import cc.lixiaohui.share.server.handler.SessionHandler;
import cc.lixiaohui.share.server.handler.message.IMessageHandler;
import cc.lixiaohui.share.server.service.AbstractService;
import cc.lixiaohui.share.server.service.util.AnnotationUtils;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.server.util.ResponseFuture;
import cc.lixiaohui.share.util.NamedThreadFactory;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

/**
 * 服务端抽象实现, 服务网络连接相关
 * @author lixiaohui
 * @date 2016年11月7日 下午9:33:21
 */
public abstract class AbstractShareServer extends AbstractLifeCycle implements IMessageHandler{
	
	protected ServerConfig config;
	
	protected ServerBootstrap bootstrap;
	protected EventLoopGroup bossGroup;
	protected EventLoopGroup workerGroup;
	protected ServerChannel serverChannel;
	
	/**
	 * Session管理者
	 */
	protected SessionManager sessionManager;
	
	/**
	 * 序列化工厂
	 */
	protected ISerializeFactory serializeFactory;
	
	/**
	 * 过程Method, 自动扫描生成
	 * servicename#procedure -> Method
	 */
	protected static final Map<String, Method> PROCEDURE_MAP = new HashMap<String, Method>();
	
	/**
	 * 业务线程池
	 */
	private ScheduledThreadPoolExecutor executor;
	
	/**
	 * 
	 * messageId -> ResponseFuture
	 */
	protected final Map<Long, ResponseFuture> futures = new ConcurrentHashMap<Long, ResponseFuture>();
	
	public static final String HN_HEARTBEAT = "Heartbeat";
	public static final String HN_DECODER = "Decoder";
	public static final String HN_ENCODER = "Encoder";
	public static final String HN_MESSAGE = "MessageHandler";
	public static final String HN_EVENT = "EventHandler";
	public static final String HN_SESSION = "Session";
	public static final String HN_FILTER = "AuthFilter";
	public static final String HN_HANDSHAKE = "HandShake";
	public static final String HN_IDLE = "IdleState";
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractShareServer.class);
	
	protected static final String SERVICE_PACKAGE = "cc.lixiaohui.share.server.service";
	
	public static final String SP_SPLITER = "#";
	
	private final HandShakeRequestMessage handshakeMessage;
	
	protected AbstractShareServer(ServerConfig config) {
		this.config = config;
		handshakeMessage = HandShakeRequestMessage.builder().heartbeatInterval(config.getSocketConfig().getHeartbeatInterval())
				.reconnectInterval(config.getSocketConfig().getReconnectInterval())
				.requestTimeout(config.getSessionConfig().getMessageTimeout())
				.reconnectTimes(config.getSocketConfig().getReconnectTimes()).build();
	}
	
	@Override
	protected void initInternal() throws LifeCycleException {
		logger.info("server initializing...");
		executor = new ScheduledThreadPoolExecutor(config.getPoolConfig().getCorePoolsize(), new NamedThreadFactory("Core-Worker"));
		initSessionManager();
		initProcedures();
		initSerializeFactory();
		initEventGroup(); 
		// must init last
		initBootstrap();
		logger.info("server initialized");
	}

	private void initSessionManager() throws LifeCycleException {
		sessionManager = new SessionManager(config.getSessionConfig(), executor);
	}

	/**
	 * 扫描cc.lixiaohui.server.service下的所有类, 查找{@link Service} 标记的类下{@link Procedure} 标记的所有方法
	 */
	private void initProcedures() {
		logger.info("initialing procedures...");
		Set<Class<?>> classes = null;
		try {
			classes = AnnotationUtils.findAnnotatedClasses(SERVICE_PACKAGE, Service.class);
		} catch (Exception e) {
			logger.error("{}", e);
		}
		if (classes != null && classes.size() > 0) {
			for (Class<?> c : classes) {
				try {
					List<Method> annotatedMethods = AnnotationUtils.findAnnotatedMethod(c, Procedure.class);
					String serviceName = c.getAnnotation(Service.class).name();
					for (Method m : annotatedMethods) {
						String procedureName = m.getAnnotation(Procedure.class).name();
						String key = serviceName + SP_SPLITER + procedureName;
						PROCEDURE_MAP.put(key, m);
						logger.info("Procedure {} mapped", key);
					}
				} catch (Exception e) {
					logger.error("{}", e);
					continue;
				}
			}
		}
		logger.info("total {} service class and {} service procedure found", classes.size(), PROCEDURE_MAP.size());
		logger.info("procedures initialized");
	}

	private void initSerializeFactory() throws LifeCycleException{
		try {
			String className = config.getSocketConfig().getSerializeFactoryClass();
			serializeFactory = (ISerializeFactory) Class.forName(className).newInstance();
		} catch (Exception e) {
			logger.error("unable to instantiate serialize factory, {}", e);
			throw new LifeCycleException(e);
		}
	}

	private void initBootstrap() {
		bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				logger.info("new channel {}", ch);
				ChannelPipeline pl = ch.pipeline();
				
				// 会话过滤
				pl.addLast(HN_SESSION, new SessionHandler(sessionManager));
				
				// 编解码器
				pl.addLast(HN_DECODER, new MessageDecoder(serializeFactory));
				pl.addLast(HN_ENCODER, new MessageEncoder(serializeFactory));
				
				pl.addLast(HN_HANDSHAKE, new HandshakeHandler(handshakeMessage, AbstractShareServer.this));
			}
			
		});
	}

	public ChannelHandler newAuthFilter() {
		return new AuthFilter(PROCEDURE_MAP);
	}
	
	public ChannelHandler newHeartbeatHandler() {
		return new HeartbeatHandler(config.getSocketConfig());
	}
	
	public ChannelHandler newIdleStateHandler() {
		return new IdleStateHandler(config.getSocketConfig().getHeartbeatInterval(), 0, 0, TimeUnit.MILLISECONDS);
	}
	
	public ChannelHandler newMessageDispatcher() {
		return new MessageDispatcher(executor, AbstractShareServer.this);
	}
	
	public ChannelHandler newSessionHandler() {
		return new SessionHandler(sessionManager);
	}
	
	private void initEventGroup() {
		int acceptorThreads = config.getSocketConfig().getAcceptorThreads();
		int ioThreads = config.getSocketConfig().getIoEventThreads();
		bossGroup = new NioEventLoopGroup(acceptorThreads, new NamedThreadFactory("IO-Acceptor"));
		workerGroup = new NioEventLoopGroup(ioThreads, new NamedThreadFactory("IO-Worker"));
	}
	
	@Override
	protected void startInternal() throws LifeCycleException {
		String bindAddress = config.getSocketConfig().getBindAddress();
		int port = config.getSocketConfig().getPort();
		try {
			serverChannel = (ServerChannel) bootstrap.bind(bindAddress, port).sync().channel();
			logger.info("Server was successfully bound to {}:{}", bindAddress, port);
		} catch (InterruptedException e) {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			throw new LifeCycleException(e);
		}
		
		serverChannel.closeFuture().addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				logger.info("Server listener shutdown");
			}
			
		});
		
		if (config.getSessionConfig().getTimeout() > 0) {
			// 会话超时检查任务
			executor.scheduleAtFixedRate(sessionManager.new TimeoutGuardian(), 100, 100, TimeUnit.MILLISECONDS);
		}
		
		logger.info("server started");
	}
	
	@Override
	protected void destroyInternal() throws LifeCycleException {
		try {
			serverChannel.close().sync();
		} catch (InterruptedException e) {
			logger.error("{}", e);
		}
		workerGroup.shutdownGracefully().syncUninterruptibly();
		bossGroup.shutdownGracefully().syncUninterruptibly();
		logger.info("server destroyed");
	}

	/**
	 * 向指定channel写信息
	 */
	protected void writeMessage(final Message message, ChannelHandlerContext ctx) {
		writeMessage(message, ctx.channel());
	}
	
	/**
	 * 向指定channel写信息
	 */
	protected void writeMessage(final Message message, Channel ch) {
		
		ch.writeAndFlush(message).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				logger.debug("write message {} to channel {}", message, future.channel());
			}
			
		});
	}

	/**
	 * 反射获取服务类实例, 构造参数{@link AbstractService#CONSTRUCTOR_TYPES}
	 */
	protected Object getServiceInstance(Method method, Session session, Map<String, Object> params) throws ServiceException {
		try {
			Class<?> clazz = method.getDeclaringClass();
			Constructor<?> con = clazz.getConstructor(AbstractService.CONSTRUCTOR_TYPES);
			return con.newInstance(session, params);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ServiceException(e);
		}
	}
	
	protected void putFuture(long id, ResponseFuture future) {
		futures.put(id, future);
	}
	
	protected ResponseFuture removeFuture(long id) {
		return futures.remove(id);
	}
	
	protected void newDetectTaskFor(ResponseFuture future) {
		executor.execute(new FutureDetectTask(future));
	}
	
	/**
	 * 消息处理任务
	 * @author lixiaohui
	 * @date 2016年11月11日 下午9:03:47
	 */
	public class MessageHandlingTask implements Runnable {

		ChannelHandlerContext ctx;
		Message message;
		
		MessageHandlingTask(ChannelHandlerContext ctx, Message message){
			this.ctx = ctx;
			this.message =  message;
		}
		
		@Override
		public void run() {
			if (message instanceof CSRequestMessage) {
				handleCSRequest(ctx, (CSRequestMessage) message);
				
			} else if (message instanceof CSCRequestMessage) {
				handleCSCRequest(ctx, (CSCRequestMessage) message);
				
			} else if (message instanceof CSCResponseMessage) {
				handleCSCResponse(ctx, (CSCResponseMessage) message);
				
			} else {
				logger.warn("cannot handle {} {}", message.getClass().getSimpleName(), message);
			}
		}
		
	}
	
	/**
	 * 检查{@ResponseFuture}是否超时
	 * @author lixiaohui
	 * @date 2016年11月15日 上午10:47:20
	 */
	private class FutureDetectTask implements Runnable {

		private ResponseFuture future;
		
		FutureDetectTask(ResponseFuture future) {
			this.future = future;
		}
		
		@Override
		public void run() {
			if (future.isDone()) {
				return;
			}
			if (TimeUtils.isTimeout(future.getSendTime(), config.getSessionConfig().getMessageTimeout())) {
				future.failed(null);
				return;
			}
			// 每10ms检查一次
			executor.schedule(new FutureDetectTask(future), 10, TimeUnit.MILLISECONDS);
		}
		
	}
}
