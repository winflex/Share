package cc.lixiaohui.share.client.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.protocol.HeartbeatMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.protocol.util.IDGenerator;

/**
 * @author lixiaohui
 * @date 2016年11月6日 下午4:11:19
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			switch (((IdleStateEvent) evt).state()) {
			case ALL_IDLE:
				break;
			case READER_IDLE:
				logger.debug("reader idle");
				writeHeartbeat(ctx);
				break;
			case WRITER_IDLE:
				logger.debug("writer idle");
				writeHeartbeat(ctx);
				break;
			}
		}
	}

	private void writeHeartbeat(ChannelHandlerContext ctx) {
		Message heartbeat = HeartbeatMessage.builder().id(IDGenerator.generate()).build();
		ctx.channel().writeAndFlush(heartbeat).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				logger.debug("wrote heartbeat");
			}
		});
	}

}
