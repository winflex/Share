package cc.lixiaohui.share.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SessionManager;
import cc.lixiaohui.share.util.TimeUtils;

/**
 * 负责处理Session相关的事件
 * <ul>
 * <li>拦截所有Inbound和Outbound IO, 创建Session, IO事件通知等<li>
 * <li>监听channel close 事件, 进而从SessionManager中移除该Session</li>
 * </ul>
 * 
 * @author lixiaohui
 * @date 2016年11月10日 下午11:45:28
 */
public class SessionHandler extends ChannelDuplexHandler{

	private SessionManager sessionManager;
	
	private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);
	
	public SessionHandler(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Session session = ctx.channel().attr(SessionManager.ATTR_SESSION).get();
		// 更新lastAccessTime
		session.accessed();
		logger.info("update last IO time for session", session);
		super.channelRead(ctx, msg);
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		// write, update session's lastAccessTime
		Session session = ctx.channel().attr(SessionManager.ATTR_SESSION).get();
		session.accessed();
		logger.info("update last IO time for session", session);
		super.write(ctx, msg, promise);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Session session = newSession(ctx);
		ctx.channel().attr(SessionManager.ATTR_SESSION).set(session); // attach session
		sessionManager.addSession(session); // hand it over to manager
		logger.info("attached session for channel {}", ctx.channel());
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO 从SessionManager中删除对应Session
		Session session = ctx.channel().attr(SessionManager.ATTR_SESSION).get(); // detach session
		sessionManager.removeSession(session); // 
		logger.info("dettached session from channel {}", ctx.channel());
		super.channelInactive(ctx);
	}
	
	/**
	 * 为新channel创建Session
	 * @param ctx
	 */
	private Session newSession(ChannelHandlerContext ctx) {
		return Session.builder().sessionId(sessionManager.generateId())
				.context(ctx)
				.createTime(TimeUtils.currentTimeMillis())
				.lastAccessTime(TimeUtils.currentTimeMillis()).build();
	}

}
