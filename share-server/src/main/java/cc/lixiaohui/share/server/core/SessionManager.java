package cc.lixiaohui.share.server.core;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.server.core.config.SessionConfig;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.future.AbstractFuture;
import cc.lixiaohui.share.util.future.IFuture;
import cc.lixiaohui.share.util.future.IFutureListener;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

/**
 *  A SessionManager manage all {@link Session} it holds
 * @author lixiaohui
 * @date 2016年11月7日 下午11:36:13
 */
public class SessionManager extends AbstractLifeCycle{
	
	public static final AttributeKey<Session> ATTR_SESSION = new AttributeKey<Session>("Session");
	
	private final AtomicLong idGenerator = new AtomicLong(0);
	
	private SessionConfig sessionConfig;
	
	/**
	 * all sessions, all connected channel has a session
	 */
	private Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	private ForbidenWordFilter wordFilter;
	
	private ScheduledExecutorService executor;
	
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
	
	public SessionManager(SessionConfig config, ScheduledExecutorService executor) {
		this.sessionConfig = config;
		this.executor = executor;
	}
	
	@Override
	protected void initInternal() throws LifeCycleException {
		ForbidenWordsHolder holder = new ForbidenWordsHolder();
		holder.init();
		wordFilter = new ForbidenWordFilter(holder);
	}
	
	/**
	 * 推送消息给除了originSession外的所有session
	 */
	public IFuture<Integer> pushToAllExcept(final Session originSession, final PushMessage message) {
		final PushFuture countFuture = new PushFuture();
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				int count = 0;
				for (Session session : sessions.values()) {
					if (session.getSessionId() == originSession.getSessionId()) { // skip source
						continue;
					}
					session.getContext().channel().writeAndFlush(message);
					++count;
				}
				countFuture.success(count);
			}
			
		});
		
		countFuture.addListener(new IFutureListener<Integer>() {
			
			@Override
			public void operationCompleted(IFuture<Integer> future) throws Exception {
				logger.info("publish message {} to total {} session", message, countFuture.get());
			}
			
		});
		return countFuture;
	}
	
	/**
	 * 推送消息给指定的用户, 若该用户不在线则消息会被忽略
	 */
	public IFuture<Integer> pushTo(int userId, final PushMessage message) {
		final Session session = getSessionByUserId(userId);
		final PushFuture pushFuture = new PushFuture();
		if (session == null) { // 指定用户不在线
			pushFuture.success(0);
			return pushFuture;
		}
		
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				session.getContext().channel().writeAndFlush(message).addListener(new ChannelFutureListener() {
					
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						pushFuture.success(future.isSuccess() ? 1 : 0);
					}
					
				});
			}
			
		});
		
		pushFuture.addListener(new IFutureListener<Integer>() {

			@Override
			public void operationCompleted(IFuture<Integer> future) throws Exception {
				if (pushFuture.getNow() > 0) {
					logger.info("push message {} to session {}", message, session);
				} else {
					logger.info("failed to push message {} to session {}", message, session);
				}
			}
			
		});
		return pushFuture;
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
			if (session.getUser().getId() == userId) {
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
			if (session.isLogined() && session.getUser().getId() == userId) {
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
		return idGenerator.getAndIncrement();
	}
	
	public SessionConfig getSessionConfig() {
		return sessionConfig;
	}
	
	public ForbidenWordFilter getWordFilter() {
		return wordFilter;
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
						removeSession(session.getUser().getId());
					}
				}
			}
		}
	}
	
	private static class PushFuture extends AbstractFuture<Integer> {
		
		public void success(int count) {
			setSuccess(count);
		}
	}
	
	
}
