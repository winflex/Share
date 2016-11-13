package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cc.lixiaohui.share.core.config.PoolConfig;
import cc.lixiaohui.share.protocol.CSRequestMessage;
import cc.lixiaohui.share.protocol.Message;

/**
 * 业务处理器
 * 
 * @author lixiaohui
 * @date 2016年11月8日 下午9:58:29
 */
public class MessageHandler extends SimpleChannelInboundHandler<Message> {

	private ThreadPoolExecutor executor;
	
	private PoolConfig config;
	
	public MessageHandler(PoolConfig config) {
		this.config = config;
		executor = new ThreadPoolExecutor(config.getCorePoolsize(), config.getMaxPoolSize(), 
				config.getKeepAliveTime(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		// 交由业务池处理, 以防查库等耗时操作长期占用IO线程
		executor.execute(new Task(ctx, msg));
	}
	
	private void handleCSRequest(ChannelHandlerContext ctx, CSRequestMessage message) {
		message.getServiceName();
		message.getParameters();
		message.getProcedureName();
	}

	/**
	 * 消息处理任务
	 * @author lixiaohui
	 * @date 2016年11月11日 下午9:03:47
	 */
	private class Task implements Runnable {

		ChannelHandlerContext ctx;
		
		Message message;
		
		Task(ChannelHandlerContext ctx, Message message){
			this.ctx = ctx;
			this.message =  message;
		}
		
		@Override
		public void run() {
			if (message instanceof CSRequestMessage) {
				
			}
		}
		
	}
	
	
}
