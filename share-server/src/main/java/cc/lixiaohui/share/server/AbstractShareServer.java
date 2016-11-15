package cc.lixiaohui.share.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import cc.lixiaohui.share.protocol.CSResponseMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.protocol.codec.MessageDecoder;
import cc.lixiaohui.share.protocol.codec.MessageEncoder;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;
import cc.lixiaohui.share.server.handler.AuthFilter;
import cc.lixiaohui.share.server.handler.HeartbeatHandler;
import cc.lixiaohui.share.server.handler.MessageDispatcher;
import cc.lixiaohui.share.server.handler.SessionHandler;
import cc.lixiaohui.share.server.handler.message.IMessageHandler;
import cc.lixiaohui.share.server.service.AbstractService;
import cc.lixiaohui.share.server.service.util.AnnotationUtils;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.NamedThreadFactory;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

import com.alibaba.fastjson.JSONObject;

/**
 * 服务端抽象实现
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
	 * 已发送但还未接收到响应的消息
	 * messageId -> MessageCache
	 */
	private final Map<Long, MessageCache> sentMessages = new ConcurrentHashMap<Long, MessageCache>();
	
	private static final String HN_HEARTBEAT = "Heartbeat";
	private static final String HN_DECODER = "Decoder";
	private static final String HN_ENCODER = "Encoder";
	private static final String HN_MESSAGE = "MessageHandler";
	private static final String HN_EVENT = "EventHandler";
	private static final String HN_SESSION = "Session";
	private static final String HN_FILTER = "AuthFilter";
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractShareServer.class);
	
	protected static final String SERVICE_PACKAGE = "cc.lixiaohui.share.server.service";
	
	public static final String SP_SPLITER = "#";
	
	protected AbstractShareServer(ServerConfig config) {
		this.config = config;
	}
	
	@Override
	protected void initInternal() throws LifeCycleException {
		executor = new ScheduledThreadPoolExecutor(config.getPoolConfig().getCorePoolsize(), new NamedThreadFactory("Core-Worker"));
		initSessionManager();
		initProcedures();
		initSerializeFactory();
		initEventGroup(); 
		// must init last
		initBootstrap();
	}

	private void initSessionManager() throws LifeCycleException {
		sessionManager = new SessionManager(config.getSessionConfig());
	}

	/**
	 * 扫描cc.lixiaohui.server.service下的所有类, 查找{@link Service} 标记的类下{@link Procedure} 标记的所有方法
	 */
	private void initProcedures() {
		logger.info("initialing procedures...");
		Class<Service> serviceClass = Service.class;
		Class<Procedure> procedureClass = Procedure.class;
		Set<Class<?>> classes = null;
		try {
			classes = AnnotationUtils.findAnnotatedClasses(SERVICE_PACKAGE, serviceClass);
		} catch (Exception e) {
			logger.error("{}", e);
		}
		if (classes != null && classes.size() > 0) {
			for (Class<?> c : classes) {
				try {
					List<Method> annotatedMethods = AnnotationUtils.findAnnotatedMethod(c, procedureClass);
					String serviceName = c.getAnnotation(serviceClass).name();
					for (Method m : annotatedMethods) {
						String procedureName = m.getAnnotation(procedureClass).name();
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
				ChannelPipeline pl = ch.pipeline();
				
				// 心跳机制, 只设读idle为heartbeatInterval, 禁用writeIdle和allIdle
				long interval = config.getSocketConfig().getHeartbeatInterval();
				pl.addFirst(HN_HEARTBEAT, new IdleStateHandler(interval, 0, 0, TimeUnit.MILLISECONDS));
				
				pl.addLast(HN_SESSION, new SessionHandler(sessionManager));
				
				// 编解码器
				pl.addLast(HN_DECODER, new MessageDecoder(serializeFactory));
				pl.addLast(HN_ENCODER, new MessageEncoder(serializeFactory));
				
				// 心跳和异常处理器(解码后才知道是否是心跳包)
				pl.addLast(HN_EVENT, new HeartbeatHandler(config.getSocketConfig()));
				
				// 权限过滤器
				pl.addLast(HN_FILTER, new AuthFilter(PROCEDURE_MAP));
				
				// 核心业务执行器
				pl.addLast(HN_MESSAGE, new MessageDispatcher(executor, AbstractShareServer.this));
				
			}
			
		});
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
		
		// 消息超时检查任务
		executor.scheduleAtFixedRate(new CachedMessageCheckTask(), 100, 100, TimeUnit.MILLISECONDS);
		// 会话超时检查任务
		executor.scheduleAtFixedRate(sessionManager.new TimeoutGuardian(), 100, 100, TimeUnit.MILLISECONDS);
		
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
	}
	
	/**
	 * TODO handle {@link CSRequestMessage}
	 */
	public void handleCSRequest(ChannelHandlerContext ctx, CSRequestMessage message) {
		String key = message.getService() + AbstractShareServer.SP_SPLITER + message.getProcedure();
		Method method = PROCEDURE_MAP.get(key);
		String result = null;
		Session session = ctx.attr(SessionManager.ATTR_SESSION).get();
		Map<String, Object> params = new HashMap<String, Object>(message.getParameterMap());
		if (method != null) { 
			// 反射调用服务 
			try {
				Object serviceInstance = getServiceInstance(method, session, params);
				// service method must produce json
				result = (String) method.invoke(serviceInstance);
			} catch (Exception e) {
				result = JSONUtils.newFailureResult(e.getMessage(), ErrorCode.SERVICE, e);
			}
		} else { 
			// 服务不存在
			result = JSONUtils.newFailureResult("服务不存在", ErrorCode.SERVICE_NOT_FOUND, "");
		}
		// 回写响应
		CSResponseMessage resp = CSResponseMessage.builder()
				.correlationId(message.getId())
				.responseJson(result).build();
		writeMessage(resp, ctx);
	}
	
	/**
	 * TODO handle {@link CSCResponseMessage}
	 * 1.从 {{@link #sentMessages} 中找对应的 {@link CSCRequestMessage}找到后往源客户端发响应CSCResponseMessage
	 * @param ctx
	 */
	public void handleCSCResponse(ChannelHandlerContext ctx, CSCResponseMessage message) {
		MessageCache ms = sentMessages.remove(message.getCorrelationId());
		if (ms == null) { // 没有关联消息, 可以认为是超时消息的响应
			logger.warn("cannot found a CSCRequestMessage correlated to CSCResponseMessage {}, probably because the request is too long time ago and removed by checker", message);
			return;
		}
		Session srcSession = ms.context.attr(SessionManager.ATTR_SESSION).get();
		if (srcSession.isLogined()) {
			// 源客户端还在线, 回写CSCResponseMessage
			writeMessage(message, srcSession.getContext());
		} else {
			// 源客户端不在线
			logger.warn("CSCResponseMessage {} could not be send back because origin client is offline", message);
		}
	}
	
	/**
	 * TODO handle {@link CSCRequestMessage}
	 * <pre>
	 * 1.判断当前用户是否登陆, 否错误, 往源客户端回写错误{@link CSCResponseMessage}
	 * 2.判断对方用户是否登陆, 否错误, 往源客户端回写错误{@link CSCResponseMessage}
	 * 3.将{@link CSCRequestMessage}转发给目标客户端, 并把源消息存起来以备接收到目标客户端的CSCResponseMessage时往源客户端回写响应
	 * </pre>
	 */
	public void handleCSCRequest(ChannelHandlerContext ctx, CSCRequestMessage message) {
		Session session = ctx.attr(SessionManager.ATTR_SESSION).get();
		if (!session.isLogined()) {
			JSONObject origin = packOriginCSC(message);
			// 回写错误
			Message m = CSCResponseMessage.builder().correlationId(message.getId())
							.responseJson(JSONUtils.newFailureResult("未登录不允许执行此操作", ErrorCode.AUTH, "", origin))
							.responseTime(TimeUtils.currentTimeMillis()).build();
			writeMessage(m, ctx);
		} else {
			Session targetSession = sessionManager.getSessionByUserId(message.getToUserId());
			// 转发给目标客户端
			writeMessage(message, targetSession.getContext());
			// 保存消息
			sentMessages.put(message.getId(), new MessageCache(message, ctx));
		}
	}

	private JSONObject packOriginCSC(CSCRequestMessage message) {
		JSONObject origin = new JSONObject();
		origin.put("toUserId", message.getToUserId());
		origin.put("content", message.getText());
		return origin;
	}
	
	/**
	 * 向指定channel写信息
	 */
	private void writeMessage(final Message message, ChannelHandlerContext ctx) {
		
		ctx.writeAndFlush(message).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				logger.debug("write message {} by channel {}", message, future.channel());
			}
			
		});
	}

	/**
	 * 反射获取服务类实例, 构造参数{@link AbstractService#CONSTRUCTOR_TYPES}
	 */
	private Object getServiceInstance(Method method, Session session, Map<String, Object> params) throws ServiceException {
		try {
			Class<?> clazz = method.getClass();
			Constructor<?> con = clazz.getConstructor(AbstractService.CONSTRUCTOR_TYPES);
			return con.newInstance(session, params);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ServiceException(e);
		}
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
	 * 封装已发送的需要响应但未接收到响应的消息
	 */
	private static class MessageCache {
		Message message;
		ChannelHandlerContext context;
		final long sendTime; //发送时间

		MessageCache(Message message, ChannelHandlerContext ctx) {
			this.message = message;
			this.context = ctx;
			sendTime = TimeUtils.currentTimeMillis();
		}
	}
	
	
	/**
	 * 检查{link {@link MessageDispatcher#sentMessages} 中的消息消息是否超时, 超时时移除该消息缓存并往源客户端回写错误
	 * @author lixiaohui
	 * @date 2016年11月15日 上午10:47:20
	 */
	private class CachedMessageCheckTask implements Runnable {

		@Override
		public void run() {
			long timeout = sessionManager.getSessionConfig().getMessageTimeout();
			Iterator<Entry<Long, MessageCache>> it = sentMessages.entrySet().iterator();
			while (it.hasNext()) {
				MessageCache mc = it.next().getValue();
				if (TimeUtils.isTimeout(mc.sendTime, timeout)) { 
					// 消息超时, 往客户端回写超时响应, 并移除消息
					JSONObject result = packOriginCSC((CSCRequestMessage) mc.message);
					Message m = CSCResponseMessage.builder().correlationId(mc.message.getId())
						.responseJson(JSONUtils.newFailureResult("消息发送超时", ErrorCode.TIMEOUT, "", result))
						.responseTime(TimeUtils.currentTimeMillis()).build();
					writeMessage(m, mc.context);
					it.remove(); // 移除
				}
			}
		}
		
	}
}
