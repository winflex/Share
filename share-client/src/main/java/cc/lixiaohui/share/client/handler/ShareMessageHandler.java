package cc.lixiaohui.share.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import cc.lixiaohui.share.client.IShareClient;
import cc.lixiaohui.share.protocol.Message;

/**
 * @author lixiaohui
 * @date 2016年11月6日 下午3:52:50
 */
public class ShareMessageHandler extends SimpleChannelInboundHandler<Message> {

	private IShareClient client;
	
	public ShareMessageHandler(IShareClient client) {
		this.client = client;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		
		
	}

}
