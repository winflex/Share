package cc.lixiaohui.share.server.service.util;

import cc.lixiaohui.share.server.Session;

/**
 * 定义服务操作所需权限等级
 * @author lixiaohui
 * @date 2016年11月15日 上午11:43:38
 */
public enum PrivilegeLevel {
	
	NOT_HANDSHAKED(0),
	
	HANDSHAKED(10),
	
	LOGGED(20),
	
	ADMIN(30);
	
	private int value;
	
	PrivilegeLevel(int value) {
		this.value = value;
	}
	
	public static boolean isQulified(PrivilegeLevel targetLevel, Session session) {
		return levelForSession(session).value >= targetLevel.value;
	}
	
	public static PrivilegeLevel levelForSession(Session session) {
		if (session.isHandshaked() && session.isLogined() && session.isAdmin()) {
			return ADMIN;
		}
		if (session.isHandshaked() && session.isLogined()) {
			return LOGGED;
		}
		if (session.isHandshaked()) {
			return HANDSHAKED;
		}
		return NOT_HANDSHAKED;
	}
}
