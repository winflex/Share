package cc.lixiaohui.share.server;

import io.netty.channel.ChannelHandlerContext;
import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.util.IBuilder;
import cc.lixiaohui.share.util.TimeUtils;

/**
 * Represent a session, holds all the neccessary informations for a client
 * 
 * @author lixiaohui
 * @date 2016年11月7日 下午11:37:53
 */
public class Session {
	
	private final long sessionId;
	
	private volatile boolean logined;
	
	/** if {@link #logined} is false, this field would be null */
	private volatile User user;
	
	private ChannelHandlerContext context;
	
	private final long createTime;
	
	/** lastest read or write time, used for checks of session timeout */
	private volatile long lastAccessTime;

	/** {@link SessionManager} which this session was bound to */
	private SessionManager sessionManager;
	
	private volatile boolean handshaked;
	
	/** heartbeat miss times */
	private int heartbeatMissTimes = 0;
	
	public Session(SessionBuilder builder) {
		this.sessionId = builder.sessionId();
		this.context = builder.context();
		this.createTime = builder.createTime();
		this.lastAccessTime = builder.lastAccessTime();
		this.logined = builder.isLogined();
		this.user = builder.user;
		this.sessionManager = builder.sessionManager();
		this.handshaked = builder.handshaked();
	}
	
	public boolean login(User user) {
		if (logined) {
			return false;
		}
		this.user = user;
		this.logined = true;
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
	
	
	public void setAdminShield(boolean shield) {
		user.setAdminForbid(shield);
	}
	
	public void setSelfShield(boolean shield) {
		user.setSelfForbid(shield);
	}
	
	public boolean isAdminShield() {
		return user.isAdminForbid();
	}
	
	public boolean isSelfShield() {
		return user.isSelfForbid();
	}
	
	// ---------------------- getters and setters -------------------
	
	public void setLogined(boolean logined) {
		this.logined = logined;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public boolean isAdmin() {
		return Role.isAdmin(user.getRole().getId());
	}

	public boolean isSuper() {
		return Role.isSuper(user.getRole().getId());
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
				.append(", user").append("=").append(user.toString())
				.append(", handshaked").append("=").append(handshaked)
				.append("]").toString();
	}
	
	
	public static class SessionBuilder implements IBuilder<Session> {

		private long sessionId;
		
		private boolean logined;
		
		private User user;
		
		private ChannelHandlerContext context;
		
		private long createTime;
		
		private long lastAccessTime;
		
		
		private SessionManager sessionManager;
		
		private boolean handshaked;
		
		@Override
		public Session build() {
			return new Session(this);
		}

		public User user() {
			return user;
		}
		
		public SessionBuilder user(User user) {
			this.user = user;
			return this;
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
		
		public SessionBuilder sessionManager(SessionManager manager) {
			this.sessionManager = manager;
			return this;
		}
		
		public SessionManager sessionManager() {
			return sessionManager;
		}
		
	}
}
