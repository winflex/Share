package cc.lixiaohui.share.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SessionManager;
import cc.lixiaohui.share.util.TimeUtils;

/**
 * 拦截所有Inbound和Outbound IO, 创建Session, IO事件通知等
 * 
 * @author lixiaohui
 * @date 2016年11月10日 下午11:45:28
 */
public class SessionAttacher extends SimpleChannelInboundHandler<Message> {

	public static final AttributeKey<Session> ATTR_SESSION = new AttributeKey<Session>("Session");
	
	private SessionManager sessionManager;
	
	public SessionAttacher(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		Session session = ctx.channel().attr(ATTR_SESSION).get();
		if (session == null) {
			// 创建session
			attachSession(ctx);
		} else {
			// 更新lastAccessTime
			session.accessed();
		}
	}

	private Session attachSession(ChannelHandlerContext ctx) {
		return Session.builder().sessionId(sessionManager.generateId())
				.context(ctx)
				.createTime(TimeUtils.currentTimeMillis())
				.lastAccessTime(TimeUtils.currentTimeMillis()).build();
	}
	
}
