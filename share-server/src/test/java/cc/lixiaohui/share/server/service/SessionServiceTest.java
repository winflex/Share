package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
import cc.lixiaohui.share.server.service.util.ServiceException;

/**
 * @author lixiaohui
 * @date 2016年11月17日 下午4:45:11
 */
public class SessionServiceTest {
	
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	@Test
	public void register() throws ServiceException {
		params.put("username", "管理员");
		params.put("password", "root");
		SessionService svc = new SessionService(null, params);
		String json = svc.registerAdmin();
		System.out.println(json);
	}
	
	
	@Test
	public void login() throws ServiceException {
		params.put("username", "李酷酷");
		params.put("password", "lllldadsa");
		Session session = Session.builder().build();
		SessionService svc = new SessionService(session, params);
		String json = svc.login();
		System.out.println(json);
		
		System.out.println(svc.logout());
	}
}
