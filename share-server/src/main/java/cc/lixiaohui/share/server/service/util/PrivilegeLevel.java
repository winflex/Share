package cc.lixiaohui.share.server.service.util;

import cc.lixiaohui.share.server.Session;

/**
 * 定义服务操作所需权限等级
 * @author lixiaohui
 * @date 2016年11月15日 上午11:43:38
 */
public enum PrivilegeLevel {
	/**未握手*/
	NOT_HANDSHAKED(0),
	
	/**已握手*/
	HANDSHAKED(10),
	
	/**登陆*/
	LOGGED(20),
	
	/**管理员*/
	ADMIN(30),
	
	/**超级管理员*/
	SUPER(40);
	
	private int value;
	
	PrivilegeLevel(int value) {
		this.value = value;
	}
	
	public boolean isQulified(Session session) {
		return levelForSession(session).value >= this.value;
	}
	
	public static PrivilegeLevel levelForSession(Session session) {
		if (session.isHandshaked() && session.isLogined() && session.isSuper()) {
			return SUPER;
		}
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
