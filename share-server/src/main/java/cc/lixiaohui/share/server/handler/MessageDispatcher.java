package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSCResponseMessage;
import cc.lixiaohui.share.protocol.CSRequestMessage;
import cc.lixiaohui.share.protocol.CSResponseMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.server.handler.message.IMessageHandler;

/**
 * 业务处理器
 * <ul>
 * <li>接收{@link CSRequestMessage}, 响应{@link CSResponseMessage} </li>
 * <li>接收{@link CSCRequestMessage}, 响应{@link CSCResponseMessage} </li>
 * </ul>
 * @author lixiaohui
 * @date 2016年11月8日 下午9:58:29
 */
public class MessageDispatcher extends SimpleChannelInboundHandler<Message> {
	
	private final ScheduledExecutorService executor;
	
	private final IMessageHandler handler;
	
	private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);
	
	public MessageDispatcher(ScheduledExecutorService executor, IMessageHandler handler) {
		this.executor = executor;
		this.handler = handler;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		// 交由业务池处理, 以防查库等耗时操作长期占用IO线程
		executor.execute(new MessageHandlingTask(ctx, msg));
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
				handler.handleCSRequest(ctx, (CSRequestMessage) message);
				logger.debug("dispatch message {}", message);
			} else if (message instanceof CSCRequestMessage) {
				handler.handleCSCRequest(ctx, (CSCRequestMessage) message);
				logger.debug("dispatch message {}", message);
			} else if (message instanceof CSCResponseMessage) {
				handler.handleCSCResponse(ctx, (CSCResponseMessage) message);
				logger.debug("dispatch message {}", message);
			} else {
				logger.warn("cannot dispatch {} {}", message.getClass().getSimpleName(), message);
			}
		}
		
	}
	
}
