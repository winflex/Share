package cc.lixiaohui.share.server.service;

import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.server.Session;

/**
 * @author lixiaohui
 * @date 2016年11月20日 下午3:48:50
 */
public class TestUtils {
	
	public static Role newRole(int id) {
		Role r = new Role();
		r.setId(id);
		return r;
	}
	
	public static User newUser(int roleId, int userId) {
		User u = new User();
		u.setAdminForbid(false);
		u.setSelfForbid(false);
		u.setId(userId);
		u.setRole(newRole(roleId));
		return u;
	}
	
	public static Session loginSession(User user) {
		Session session = Session.builder().build();
		session.login(user);
		return session;
	}
	
	public static Session unloginSession() {
		return Session.builder().build();
	}
}
