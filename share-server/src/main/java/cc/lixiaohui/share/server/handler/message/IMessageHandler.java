package cc.lixiaohui.share.server.handler.message;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午9:06:33
 */
public interface IMessageHandler<T> {
	
	public void handle(ChannelHandlerContext ctx, T message) throws Exception;
	
}
