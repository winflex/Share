package cc.lixiaohui.share.server;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.core.config.ServerConfig;
import cc.lixiaohui.share.model.util.DaoFactory;
import cc.lixiaohui.share.protocol.codec.MessageDecoder;
import cc.lixiaohui.share.protocol.codec.MessageEncoder;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;
import cc.lixiaohui.share.server.handler.AuthFilter;
import cc.lixiaohui.share.server.handler.HeartbeatHandler;
import cc.lixiaohui.share.server.handler.MessageHandler;
import cc.lixiaohui.share.server.handler.SessionAttacher;
import cc.lixiaohui.share.util.NamedThreadFactory;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

/**
 * 服务端抽象实现
 * @author lixiaohui
 * @date 2016年11月7日 下午9:33:21
 */
public abstract class AbstractShareServer extends AbstractLifeCycle {
	
	protected ServerConfig config;
	
	protected ServerBootstrap bootstrap;
	protected EventLoopGroup bossGroup;
	protected EventLoopGroup workerGroup;
	protected ServerChannel serverChannel;
	
	protected SessionManager sessionManager;
	protected ISerializeFactory serializeFactory;
	protected DaoFactory daoFactory;
	
	private static final String HN_HEARTBEAT = "Heartbeat";
	private static final String HN_DECODER = "Decoder";
	private static final String HN_ENCODER = "Encoder";
	private static final String HN_MESSAGE = "MessageHandler";
	private static final String HN_EVENT = "EventHandler";
	private static final String HN_SESSION = "Session";
	private static final String HN_FILTER = "AuthFilter";
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractShareServer.class);
	
	protected AbstractShareServer(ServerConfig config) {
		this.config = config;
	}
	
	@Override
	protected void initInternal() throws LifeCycleException {
		sessionManager = new SessionManager(config.getSessionConfig());
		sessionManager.init();
		
		daoFactory = new DaoFactory();
		
		initSerializeFactory();
		initEventGroup(); 
		initBootstrap();
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
				
				// 编解码器
				pl.addLast(HN_DECODER, new MessageDecoder(serializeFactory));
				pl.addLast(HN_ENCODER, new MessageEncoder(serializeFactory));
				
				pl.addLast(HN_SESSION, new SessionAttacher(sessionManager));
				
				// 心跳和异常处理器(解码后才知道是否是心跳包)
				pl.addLast(HN_EVENT, new HeartbeatHandler(config.getSocketConfig()));
				
				// 权限过滤器
				pl.addLast(HN_FILTER, new AuthFilter());
				
				// 核心业务执行器
				pl.addLast(HN_MESSAGE, new MessageHandler(config.getPoolConfig()));
				
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
		sessionManager.destroy();
	}
	
}
