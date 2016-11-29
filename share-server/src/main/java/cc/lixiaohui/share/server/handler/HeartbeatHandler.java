package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.protocol.HeartbeatMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.server.core.SessionManager;
import cc.lixiaohui.share.server.core.config.SocketConfig;

/**
 * 心跳处理器
 * <pre>
 * 1.每当收到任意包时清空计数, 无需判断是不是心跳包, 因为所有信息都可以算是心跳包
 * 2.readIdle事件时判断计数是否到最大值, 是的话关闭连接, 否则计数加1
 * </pre>
 * 
 * @author lixiaohui
 * @date 2016年11月8日 下午11:04:04
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<Message> {
	
	private SocketConfig config;
	
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);
	
	private final long maxIdleSeconds;
	
	public HeartbeatHandler(SocketConfig config) {
		this.config = config;
		maxIdleSeconds = config.getHeartbeatInterval() * config.getMaxHeartbeatMissTimes();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		clearCount(ctx);
		// 过滤heartbeat
		if (msg instanceof HeartbeatMessage) { // 心跳
			logger.debug("recieved heartbeat from {}", ctx.channel());
			return;
		}
		ctx.fireChannelRead(msg);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		/*
		 * TODO 此处只可能有read idle, 情况发生时应该判断之前发生的idle次数, 若到达阈值, 则关闭该连接
		 */
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			switch (event.state()) {
			case READER_IDLE:
				incCount(ctx);
				if (reachTop(ctx)) {
					logger.warn("channel {} has not send any Packet for the passed {} seconds, will close the channel", ctx.channel(), maxIdleSeconds);
					ctx.close();
				}
				break;
			case WRITER_IDLE: // ignore
				break;
			case ALL_IDLE: // ignore
				break;
			default:
				break;
			}
		}
	}
	
	private void clearCount(ChannelHandlerContext ctx) {
		ctx.channel().attr(SessionManager.ATTR_SESSION).get().clearHeartbeatMissTimes();;
	}
	
	private void incCount(ChannelHandlerContext ctx) {
		ctx.channel().attr(SessionManager.ATTR_SESSION).get().incHeartbeatMissTimes();
	}
	
	private boolean reachTop(ChannelHandlerContext ctx) {
		return ctx.channel().attr(SessionManager.ATTR_SESSION).get().getHeartbeatMissTimes() == config.getMaxHeartbeatMissTimes();
	}
}
