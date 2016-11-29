package cc.lixiaohui.share.server;

import io.netty.channel.ChannelHandlerContext;
import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSCResponseMessage;
import cc.lixiaohui.share.protocol.CSRequestMessage;

/**
 * @author lixiaohui
 * @date 2016年11月15日 下午3:11:31
 */
public interface IMessageHandler {
	
	void handleCSCRequest(ChannelHandlerContext ctx, CSCRequestMessage message);
	
	void handleCSCResponse(ChannelHandlerContext ctx, CSCResponseMessage message);
	
	void handleCSRequest(ChannelHandlerContext ctx, CSRequestMessage message);
	
}
