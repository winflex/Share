package cc.lixiaohui.share.server;

import cc.lixiaohui.share.server.handler.SessionAttacher;
import cc.lixiaohui.share.util.IBuilder;
import cc.lixiaohui.share.util.TimeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 代表会话, Sessoin都是被绑定到一个个的{@link Channel} 上的
 * 
 * @author lixiaohui
 * @date 2016年11月7日 下午11:37:53
 */
public class Session {
	
	/**
	 * 唯一标识
	 */
	private final long sessionId;
	
	/**
	 * 是否登陆
	 */
	private volatile boolean logined;
	
	/**
	 * 用户ID
	 */
	private volatile int userId; 
	
	/**
	 * 用户名
	 */
	private volatile String username; 
	
	/**
	 * 自己是否屏蔽了自己
	 */
	private volatile boolean selfShield; 
	
	/**
	 * 管理员是否屏蔽了自己
	 */
	private volatile boolean adminSheid; 
	
	/**
	 * Channel 上下文
	 */
	private ChannelHandlerContext context;
	
	/** 
	 * 所持有的连接的连接建立时间 
	 **/
	private final long createTime;
	
	/**
	 * 最后访问(IO)时间, 用于控制会话超时
	 */
	private volatile long lastAccessTime;

	
	public Session(SessionBuilder builder) {
		this.sessionId = builder.sessionId();
		this.context = builder.context();
		this.createTime = builder.createTime();
		this.lastAccessTime = builder.lastAccessTime();
		this.logined = builder.isLogined();
		this.userId = builder.userId();
		this.username = builder.username();
	}
	
	public boolean login(int userId, String username, boolean selfShield, boolean adminShield) {
		if (logined) { // 已登陆
			return false;
		}
		this.userId = userId;
		this.username = username;
		this.selfShield = selfShield;
		this.adminSheid = adminShield;
		logined = true; 
		return true;
	}
	
	public boolean logout() {
		if (!logined) { // 未登陆
			return false;
		}
		this.userId = -1;
		this.username = null;
		this.logined = false; 
		return true;
	}
	
	/**
	 * 销毁session, 三种情况下会被执行:
	 * <ul>
	 * <li>注销时执行</li>
	 * <li>若开启了timeout功能, 则timeout时执行</li>
	 * <li>{@link SessionManager} 摧毁时执行</li
	 * </ul>
	 */
	public void destroy() {
		if (context != null) {
			context.attr(SessionAttacher.ATTR_SESSION).set(null);
		}
	}
	
	/**
	 * IO事件通知
	 */
	public void accessed() {
		setLastAccessTime(TimeUtils.currentTimeMillis());
	}
	
	// ---------------------- getters and setters -------------------
	
	public void setLogined(boolean logined) {
		this.logined = logined;
	}

	public boolean isSelfShield() {
		return selfShield;
	}

	public void setSelfShield(boolean selfShield) {
		this.selfShield = selfShield;
	}

	public boolean isAdminSheid() {
		return adminSheid;
	}

	public void setAdminSheid(boolean adminSheid) {
		this.adminSheid = adminSheid;
	}

	public long getSessionId() {
		return sessionId;
	}

	public ChannelHandlerContext getContext() {
		return context;
	}

	public void setContext(ChannelHandlerContext context) {
		this.context = context;
	}

	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the ctx
	 */
	public ChannelHandlerContext getCtx() {
		return context;
	}

	/**
	 * @param ctx the ctx to set
	 */
	public void setCtx(ChannelHandlerContext ctx) {
		this.context = ctx;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public boolean isLogined() {
		return logined;
	}
	
	public static SessionBuilder builder() {
		return new SessionBuilder();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public static class SessionBuilder implements IBuilder<Session> {

		private long sessionId;
		
		private boolean logined;
		
		private int userId;
		
		private String username;
		
		private ChannelHandlerContext context;
		
		private long createTime;
		
		private long lastAccessTime;
		
		@Override
		public Session build() {
			return new Session(this);
		}

		public long sessionId() {
			return sessionId;
		}

		public SessionBuilder sessionId(long sessionId) {
			this.sessionId = sessionId;
			return this;
		}

		public boolean isLogined() {
			return logined;
		}

		public SessionBuilder logined(boolean logined) {
			this.logined = logined;
			return this;
		}

		public int userId() {
			return userId;
		}

		public SessionBuilder userId(int userId) {
			this.userId = userId;
			return this;
		}

		public ChannelHandlerContext context() {
			return context;
		}

		public SessionBuilder context(ChannelHandlerContext context) {
			this.context = context;
			return this;
		}

		public long createTime() {
			return createTime;
		}

		public SessionBuilder createTime(long createTime) {
			this.createTime = createTime;
			return this;
		}

		public long lastAccessTime() {
			return lastAccessTime;
		}

		public SessionBuilder lastAccessTime(long lastAccessTime) {
			this.lastAccessTime = lastAccessTime;
			return this;
		}
		
		public String username() {
			return username;
		}
		
		public SessionBuilder username(String username) {
			this.username = username;
			return this;
		}

	}
}