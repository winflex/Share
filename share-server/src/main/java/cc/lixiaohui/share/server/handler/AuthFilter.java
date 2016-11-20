package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;

import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSRequestMessage;
import cc.lixiaohui.share.protocol.CSResponseMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.server.AbstractShareServer;
import cc.lixiaohui.share.server.SessionManager;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

/**
 * 对权限进行过滤, 如未登陆的客户端不能发送消息等
 * 
 * @author lixiaohui
 * @date 2016年11月10日 下午9:54:50
 */
public class AuthFilter extends SimpleChannelInboundHandler<Message> {

	
	private final Map<String, Method> PROCEDURE_MAP;
	
	public AuthFilter(Map<String, Method> map) {
		this.PROCEDURE_MAP = map;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		
		if (msg instanceof CSRequestMessage) { // 据接口不同权限不同
			CSRequestMessage req = (CSRequestMessage) msg;
			Method method = PROCEDURE_MAP.get(req.getService() + AbstractShareServer.SP_SPLITER + req.getProcedure());
			
			boolean qulified = method.getAnnotation(Procedure.class).level()
					.isQulified(ctx.channel().attr(SessionManager.ATTR_SESSION).get());
			if (!qulified) { // 不符合权限
				CSResponseMessage resp = CSResponseMessage.builder().correlationId(req.getId())
						.responseJson(JSONUtils.newFailureResult("未登录或权限不足", ErrorCode.AUTH, "")).build();
				ctx.writeAndFlush(resp);
				return;
			}
		}
		
		if (msg instanceof CSCRequestMessage) { // 聊天消息必须登陆
			CSCRequestMessage req = (CSCRequestMessage) msg;
			if (!ctx.channel().attr(SessionManager.ATTR_SESSION).get().isLogined()) {
				CSResponseMessage resp = CSResponseMessage.builder().correlationId(req.getId())
						.responseJson(JSONUtils.newFailureResult("未登录或权限不足", ErrorCode.AUTH, "")).build();
				ctx.writeAndFlush(resp);
				return;
			}
		}
		
		ctx.fireChannelRead(msg);
	}

}
