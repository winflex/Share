package cc.lixiaohui.share.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import cc.lixiaohui.share.util.IBuilder;
import cc.lixiaohui.share.util.TimeUtils;

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
	 * 是否是管理员
	 */
	private volatile boolean admin;
	
	/**
	 * 自己是否屏蔽了自己
	 */
	private volatile boolean selfShield; 
	
	/**
	 * 管理员是否屏蔽了自己
	 */
	private volatile boolean adminShield; 
	
	
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

	/**
	 * 管理该Session的SessionManager
	 */
	private SessionManager sessionManager;
	
	private volatile boolean handshaked;
	
	private int heartbeatMissTimes = 0;
	
	public Session(SessionBuilder builder) {
		this.sessionId = builder.sessionId();
		this.context = builder.context();
		this.createTime = builder.createTime();
		this.lastAccessTime = builder.lastAccessTime();
		this.logined = builder.isLogined();
		this.userId = builder.userId();
		this.username = builder.username();
		this.admin = builder.admin();
		this.sessionManager = builder.sessionManager();
		this.handshaked = builder.handshaked();
	}
	
	public boolean login(int userId, String username, boolean selfShield, boolean adminShield) {
		if (logined) { // 已登陆
			return false;
		}
		this.userId = userId;
		this.username = username;
		this.selfShield = selfShield;
		this.adminShield = adminShield;
		logined = true; 
		return true;
	}
	
	public boolean logout() {
		if (!logined) { // 未登陆
			return false;
		}
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
			context.attr(SessionManager.ATTR_SESSION).set(null);
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

	public boolean isAdminShield() {
		return adminShield;
	}

	public void setAdminShield(boolean adminSheild) {
		this.adminShield = adminSheild;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
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
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public boolean isHandshaked() {
		return handshaked;
	}

	public void setHandshaked(boolean handshaked) {
		this.handshaked = handshaked;
	}

	public int getHeartbeatMissTimes() {
		return heartbeatMissTimes;
	}

	public void clearHeartbeatMissTimes() {
		this.heartbeatMissTimes = 0;
	}
	
	public void incHeartbeatMissTimes() {
		++heartbeatMissTimes;
	}
	
	
	@Override
	public String toString() {
		
		return new StringBuilder("Session[")
				.append("id").append("=").append(sessionId)
				.append(", logined").append("=").append(logined)
				.append(", username").append("=").append(username)
				.append(", selfShield").append("=").append(selfShield)
				.append(", adminShield").append("=").append(adminShield)
				.append(", handshaked").append("=").append(handshaked)
				.append("]").toString();
	}
	
	
	public static class SessionBuilder implements IBuilder<Session> {

		private long sessionId;
		
		private boolean logined;
		
		private int userId;
		
		private String username;
		
		private ChannelHandlerContext context;
		
		private long createTime;
		
		private long lastAccessTime;
		
		private boolean selfShield;
		
		private boolean adminShield;
		
		private SessionManager sessionManager;
		
		private boolean handshaked;
		
		private boolean admin;
		
		@Override
		public Session build() {
			return new Session(this);
		}

		public boolean handshaked() {
			return handshaked;
		}

		public SessionBuilder handshaked(boolean handshaked) {
			this.handshaked = handshaked;
			return this;
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
		
		public SessionBuilder selfShield(boolean selfShield) {
			this.selfShield = selfShield;
			return this;
		}
		
		public boolean selfShield() {
			return selfShield;
		}
		
		public boolean adminShield() {
			return adminShield;
		}
		
		public SessionBuilder adminShield(boolean adminShield) {
			this.adminShield = adminShield;
			return this;
		}
		
		public SessionBuilder sessionManager(SessionManager manager) {
			this.sessionManager = manager;
			return this;
		}
		
		public SessionManager sessionManager() {
			return sessionManager;
		}
		
		public boolean admin() {
			return admin;
		}
		
		public SessionBuilder admin(boolean admin) {
			this.admin = admin;
			return this;
		}
	}
}
