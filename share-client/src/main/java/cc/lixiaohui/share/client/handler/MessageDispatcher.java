package cc.lixiaohui.share.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSResponseMessage;
import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.protocol.PushMessage;

/**
 * 负责消息分发
 * 
 * @author lixiaohui
 * @date 2016年11月6日 下午3:52:50
 */
public class MessageDispatcher extends SimpleChannelInboundHandler<Message> {

	private IMessageHandler handler;

	private ExecutorService executor;

	private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

	public MessageDispatcher(IMessageHandler handler, ExecutorService executor) {
		this.handler = handler;
		this.executor = executor;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, final Message msg) throws Exception {
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				if (msg instanceof HandShakeRequestMessage) {
					logger.info("dispatched {} messsage {}", msg.getClass().getSimpleName(), msg);
					handler.handleHandshake(ctx, (HandShakeRequestMessage) msg);

				} else if (msg instanceof CSResponseMessage) {
					logger.info("dispatched {} messsage {}", msg.getClass().getSimpleName(), msg);
					handler.handleCSResponse(ctx, (CSResponseMessage) msg);

				} else if (msg instanceof CSCRequestMessage) {
					logger.info("dispatched {} messsage {}", msg.getClass().getSimpleName(), msg);
					handler.handleCSCRequest(ctx, (CSCRequestMessage) msg);

				} else if (msg instanceof PushMessage) {
					logger.info("dispatched {} messsage {}", msg.getClass().getSimpleName(), msg);
					handler.handlePushMessage(ctx, (PushMessage) msg);
				} else {
					logger.warn("cannot handle {} message {}", msg.getClass().getSimpleName(), msg);
					return;
				}
			}
			
		});
	}

}
