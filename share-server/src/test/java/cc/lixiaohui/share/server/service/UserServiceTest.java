package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.SystemRuntime;

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
	
}
