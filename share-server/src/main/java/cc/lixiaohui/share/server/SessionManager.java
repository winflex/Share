package cc.lixiaohui.share.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import cc.lixiaohui.share.core.config.SessionConfig;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

/**
 * 会话管理者
 * @author lixiaohui
 * @date 2016年11月7日 下午11:36:13
 */
public class SessionManager extends AbstractLifeCycle{
	
	private final AtomicLong SESSION_ID_GENERATOR = new AtomicLong(0);
	
	private SessionConfig config;
	
	/**
	 * 所有由该SessionManager管理的Session
	 */
	private Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	/**
	 * 超时守护者
	 */
	private TimeoutGuardian timeoutGuardian;
	
	
	public SessionManager(SessionConfig config) {
		this.config = config;
	}
	
	
	@Override
	protected void initInternal() throws LifeCycleException {
		if (config.getTimeout() > 0) {
			timeoutGuardian = new TimeoutGuardian();
		}
	}
	
	@Override
	protected void startInternal() throws LifeCycleException {
		if (timeoutGuardian != null) {
			timeoutGuardian.start();
		}
	}
	
	@Override
	protected void destroyInternal() throws LifeCycleException {
		if (timeoutGuardian != null) {
			timeoutGuardian.stop();
		}
		sessions.clear();
	}
	
	public void addSession(Session session) {
		sessions.put(session.getSessionId(), session);
	}
	
	public void addSessionIfAbsent(Session session) {
		if (!sessions.containsKey(session.getSessionId())) {
			sessions.put(session.getSessionId(), session);
		}
	}
	
	public Session getSession(int userId) {
		return sessions.get(userId);
	}
	
	public Session removeSession(int userId) {
		return sessions.remove(userId);
	}
	
	public boolean contains(int userId) {
		return sessions.containsKey(userId);
	}
	
	private boolean isTimeout(Session session) {
		return session.getLastAccessTime() + config.getTimeout() > TimeUtils.currentTimeMillis();
	}
	
	public long generateId() {
		return SESSION_ID_GENERATOR.getAndIncrement();
	}
	
	private class TimeoutGuardian implements Runnable{
		
		private Thread worker;
		
		private volatile boolean stop = false; 

		public void start() {
			worker = new Thread(this);
			worker.start();
		}
		
		public void stop() {
			stop = true;
		}
		
		@Override
		public void run() {
			while (!stop) {
				for (Session session : sessions.values()) {
					if (isTimeout(session)) {
						removeSession(session.getUserId());
					}
				}
			}
		}
	}
}
