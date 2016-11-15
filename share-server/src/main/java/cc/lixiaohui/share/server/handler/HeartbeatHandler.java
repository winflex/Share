package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.core.config.SocketConfig;
import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.HandshakeResponseMessage;
import cc.lixiaohui.share.protocol.HeartbeatMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.server.SessionManager;

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
	
	private final Message handShakeMessage;
	
	private SocketConfig config;
	
	private static final AttributeKey<Integer> ATTR_MISS_TIMES = new AttributeKey<Integer>("HeartMissTimes");
	
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);
	
	private final long maxIdleSeconds;
	
	public HeartbeatHandler(SocketConfig config) {
		this.config = config;
		handShakeMessage = HandShakeRequestMessage.builder().heartbeatInterval(config.getHeartbeatInterval()).build();
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
		if (msg instanceof HandshakeResponseMessage) { // 握手响应
			ctx.attr(SessionManager.ATTR_SESSION).get().setHandshaked(true);
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
					logger.warn("channel {} has not send any Packet for the passed {} seconds", ctx.channel(), maxIdleSeconds);
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
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		/*
		 * TODO channel刚连接时, 给其发个握手包
		 */
		ctx.channel().attr(ATTR_MISS_TIMES).set(0);
		ctx.writeAndFlush(handShakeMessage);
	}
	
	
	private void clearCount(ChannelHandlerContext ctx) {
		ctx.channel().attr(ATTR_MISS_TIMES).set(0);
	}
	
	private void incCount(ChannelHandlerContext ctx) {
		int count = ctx.channel().attr(ATTR_MISS_TIMES).get();
		ctx.channel().attr(ATTR_MISS_TIMES).set(count + 1);
	}
	
	private boolean reachTop(ChannelHandlerContext ctx) {
		return ctx.channel().attr(ATTR_MISS_TIMES).get() == config.getMaxHeartbeatMissTimes();
	}
}
