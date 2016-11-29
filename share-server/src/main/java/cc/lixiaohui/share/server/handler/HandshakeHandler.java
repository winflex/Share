package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.HandshakeResponseMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.server.AbstractShareServer;
import cc.lixiaohui.share.server.core.SessionManager;

/**
 * 握手处理
 * <pre>
 * full handler chain before handshake:
 * MessageDecoder -> MessageEncoder -> HandshakeHandler
 * 
 * full handler chain after handshake:
 * [IdleStateHandler ->] MessageDecoder -> MessageEncoder [-> HeartbeatHandler] -> SessionHandler -> AuthFilter -> MessageDispatcher
 * 
 * </pre>
 * @author lixiaohui
 * @date 2016年11月17日 上午10:55:50
 */
public class HandshakeHandler extends SimpleChannelInboundHandler<Message> {

	private final HandShakeRequestMessage handShakeMessage;
	
	private AbstractShareServer server;
	
	//private SocketConfig config;
	
	private static final Logger logger = LoggerFactory.getLogger(HandshakeHandler.class);
	
	public HandshakeHandler(HandShakeRequestMessage message, AbstractShareServer server) {
		this.handShakeMessage = message;
		this.server = server;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		if (!(msg instanceof HandshakeResponseMessage)) { // 握手响应
			logger.warn("unable to handle message {} before handshake", msg);
			return;
		}
		logger.info("recieved handshake response from channel {}", ctx.channel());
		ctx.channel().attr(SessionManager.ATTR_SESSION).get().setHandshaked(true);
		ChannelPipeline pl = ctx.channel().pipeline();
		// 移除握手处理器
		pl.remove(AbstractShareServer.HN_HANDSHAKE); 
		
		// add idlestate handler
		long interval = handShakeMessage.getHeartbeatInterval();
		if (interval > 0){ // 开启心跳
			pl.addFirst(AbstractShareServer.HN_IDLE, new IdleStateHandler(interval, 0, 0, TimeUnit.MILLISECONDS));
			pl.addAfter(AbstractShareServer.HN_ENCODER, AbstractShareServer.HN_HEARTBEAT, server.newHeartbeatHandler());
		}
		pl.addLast(AbstractShareServer.HN_FILTER, server.newAuthFilter());
		pl.addLast(AbstractShareServer.HN_MESSAGE, server.newMessageDispatcher());
		logger.info("connection established {}", ctx.channel());
		return;
	}

	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(handShakeMessage).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				logger.info("write handshake to channel {}", future.channel());
			}
		});
		super.channelActive(ctx);
	}
}
