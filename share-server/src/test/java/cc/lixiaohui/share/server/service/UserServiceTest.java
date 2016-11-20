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
	
	@Test
	public void getUser() throws ServiceException {
		params.put("userId", 7);
		UserService svc = new UserService(null, params);
		String json = svc.getUser();
		System.out.println(json);
	}
	
	@Test
	public void update() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		
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
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.shield();
		System.out.println(json);
	}
	
	@Test
	public void adminShield() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.shield();
		System.out.println(json);
		System.out.println(session.isAdminShield());
		System.out.println(session.isSelfShield());
	}
	@Test
	public void selfUnshield() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.unshield();
		System.out.println(json);
		System.out.println(session.isAdminShield());
		System.out.println(session.isSelfShield());
	}
	@Test
	public void adminUnshield() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		
		params.put("userId", 7);
		UserService svc = new UserService(session, params);
		String json = svc.unshield();
		System.out.println(json);
		System.out.println(session.isAdminShield());
		System.out.println(session.isSelfShield());
	}
	
	
	@Test
	public void search() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		
		
		params.put("keyword", "辉");
		UserService svc = new UserService(session, params);
		String json = svc.searchUser();
		System.out.println(json);
	}
	
	@Test
	public void addFriend() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		
		params.put("targetUserId", 7);
		
		UserService svc = new UserService(session, params);
		String json = svc.addFriend();
		
		System.out.println(json);
	}
	
	@Test
	public void getFriends() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 5));
		
		UserService svc = new UserService(session, params);
		String json = svc.getFriends();
		System.out.println(json);
	}
	@Test
	public void deleteFriend() {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 5));
		
		params.put("friendShipId", 1);
		
		UserService svc = new UserService(session, params);
		String json = svc.deleteFriend();
		System.out.println(json);
	}
	
}
