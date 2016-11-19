package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
import cc.lixiaohui.share.server.service.util.ServiceException;

/**
 * @author lixiaohui
 * @date 2016年11月13日 下午12:51:42
 */
public class UserServiceTest {
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	Session session = Session.builder().userId(7).build();
	
	@Test
	public void getUser() throws ServiceException {
		params.put("userId", 7);
		UserService svc = new UserService(null, params);
		String json = svc.getUser();
		System.out.println(json);
	}
	
	@Test
	public void update() {
		Session session = Session.builder().userId(7).build();
		params.put("signature", "7777777777");
		params.put("sex", "男");
		params.put("headImageId", 4);
		params.put("password", "7777");
		UserService svc = new UserService(session, params);
		String json = svc.updateUser();
		System.out.println(json);
	}
	
	@Test
	public void selfShield() {
		Session session = Session.builder().userId(7).build();
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.shield();
		System.out.println(json);
	}
	
	@Test
	public void adminShield() {
		Session session = Session.builder().userId(4).build();
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.shield();
		System.out.println(json);
		System.out.println(session.isAdminShield());
		System.out.println(session.isSelfShield());
	}
	@Test
	public void selfUnshield() {
		Session session = Session.builder().userId(7).build();
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.unshield();
		System.out.println(json);
		System.out.println(session.isAdminShield());
		System.out.println(session.isSelfShield());
	}
	@Test
	public void adminUnshield() {
		Session session = Session.builder().userId(4).build();
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.unshield();
		System.out.println(json);
		System.out.println(session.isAdminShield());
		System.out.println(session.isSelfShield());
	}
	
	
	@Test
	public void search() {
		Session session = Session.builder().userId(4).build();
		
		params.put("keyword", "辉");
		UserService svc = new UserService(session, params);
		String json = svc.searchUser();
		System.out.println(json);
	}
	
	@Test
	public void addFriend() {
		Session session = Session.builder().build();
		session.login(6, "老王", false, false);
		
		params.put("targetUserId", 7);
		
		UserService svc = new UserService(session, params);
		String json = svc.addFriend();
		
		System.out.println(json);
	}
}
