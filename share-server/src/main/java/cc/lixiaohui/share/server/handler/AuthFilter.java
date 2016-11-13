package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashSet;
import java.util.Set;

import cc.lixiaohui.share.protocol.Message;

/**
 * 对权限进行过滤, 如未登陆的客户端不能发送消息等
 * 
 * @author lixiaohui
 * @date 2016年11月10日 下午9:54:50
 */
public class AuthFilter extends SimpleChannelInboundHandler<Message> {

	private static Set<Class<? extends Message>> strictMessageMap = new HashSet<Class<? extends Message>>();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		
	}

}
