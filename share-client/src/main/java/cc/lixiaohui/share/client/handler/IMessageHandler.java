package cc.lixiaohui.share.client.handler;

import io.netty.channel.ChannelHandlerContext;
import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSCResponseMessage;
import cc.lixiaohui.share.protocol.CSResponseMessage;
import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.PushMessage;

/**
 * Message handler
 * @author lixiaohui
 * @date 2016年11月16日 下午1:42:35
 */
public interface IMessageHandler {
	
	void handleHandshake(ChannelHandlerContext ctx, HandShakeRequestMessage message);
	
	void handleCSResponse(ChannelHandlerContext ctx, CSResponseMessage message);
	
	void handleCSCRequest(ChannelHandlerContext ctx, CSCRequestMessage message);
	
	void handleCSCResponse(ChannelHandlerContext ctx, CSCResponseMessage message);
	
	void handlePushMessage(ChannelHandlerContext ctx, PushMessage message);
	
}
