package cc.lixiaohui.share.server;

import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.server.config.SessionConfig;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.future.AbstractFuture;
import cc.lixiaohui.share.util.future.IFuture;
import cc.lixiaohui.share.util.future.IFutureListener;

/**
 * 会话管理者
 * @author lixiaohui
 * @date 2016年11月7日 下午11:36:13
 */
public class SessionManager {
	
	public static final AttributeKey<Session> ATTR_SESSION = new AttributeKey<Session>("Session");
	
	private final AtomicLong SESSION_ID_GENERATOR = new AtomicLong(0);
	
	private SessionConfig sessionConfig;
	
	/**
	 * 所有由该SessionManager管理的Session, key为sessionId而不是userId因为一个session不一定有userId, 因为未登录也能执行某些操作
	 */
	private Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
	
	private ScheduledExecutorService executor;
	
	public SessionManager(SessionConfig config, ScheduledExecutorService executor) {
		this.sessionConfig = config;
		this.executor = executor;
	}
	
	/**
	 * 推送消息
	 * @param originSession
	 * @return 异步推送结果
	 */
	public IFuture<Integer> publishPushMessage(final Session originSession, final PushMessage message) {
		final CountFuture countFuture = new CountFuture();
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				int count = 0;
				for (Session session : sessions.values()) {
					if (session.getSessionId() == originSession.getSessionId()) { // skip source
						continue;
					}
					session.getContext().writeAndFlush(message);
					++count;
				}
				countFuture.success(count);
			}
		});
		countFuture.addListener(new IFutureListener<Integer>() {
			
			@Override
			public void operationCompleted(IFuture<Integer> future) throws Exception {
				logger.info("publish message {} to total {} client", message, countFuture.get());
			}
		});
		return countFuture;
	}
	
	/**
	 * 根据用户Id获取Session, 若返回非null值的话就一定说明该session是已登陆的
	 * @param userId yonghuID
	 * @return 关联的Session或null, 如果没有任何关联的话
	 */
	public Session getSessionByUserId(int userId) {
		for (Session session : sessions.values()) {
			if (!session.isLogined()) {
				continue;
			}
			if (session.getUserId() == userId) {
				return session;
			}
		}
		return null;
	}
	
	/**
	 * 根据session的id获取session
	 * @param sessionId 
	 * @return 关联的session
	 */
	public Session getSessionBySessionId(long sessionId) {
		return sessions.get(sessionId);
	}
	
	/**
	 * 判断用户是否登陆
	 * @param userId
	 * @return
	 */
	public boolean isUserLogined(int userId) {
		for (Session session : sessions.values()) {
			if (session.isLogined() && session.getUserId() == userId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 新增Session
	 * @param session
	 */
	public void addSession(Session session) {
		sessions.put(session.getSessionId(), session);
	}
	
	/**
	 * 移除Session
	 * @param sessionId
	 */
	public void removeSession(long sessionId) {
		sessions.remove(sessionId);
	}
	
	/**
	 * 移除session
	 * @param session
	 */
	public void removeSession(Session session) {
		sessions.remove(session.getSessionId());
	}
	
	public void addSessionIfAbsent(Session session) {
		if (!sessions.containsKey(session.getSessionId())) {
			sessions.put(session.getSessionId(), session);
		}
	}
	
	public long generateId() {
		return SESSION_ID_GENERATOR.getAndIncrement();
	}
	
	public SessionConfig getSessionConfig() {
		return sessionConfig;
	}
	
	/**
	 * 会话超时探测任务
	 */
	public class TimeoutGuardian implements Runnable{
		
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
					if (TimeUtils.isTimeout(session.getLastAccessTime(), sessionConfig.getTimeout())) {
						logger.info("session timeout, removed {}", session);
						removeSession(session.getUserId());
					}
				}
			}
		}
	}
	
	private class CountFuture extends AbstractFuture<Integer> {
		
		public void success(int count) {
			setSuccess(count);
		}
	}
}
