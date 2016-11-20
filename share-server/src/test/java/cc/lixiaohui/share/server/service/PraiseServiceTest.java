package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
import cc.lixiaohui.share.server.service.util.ServiceException;

/**
 * @author lixiaohui
 * @date 2016年11月20日 下午6:41:34
 */
public class PraiseServiceTest {
	
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	@Test
	public void like() throws ServiceException {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 5));
		
		params.put("shareId", 1);
		
		PraiseService svc = new PraiseService(session, params);
		
		String json = svc.like();
		System.out.println(json);
	}
	
	@Test
	public void unlike() throws ServiceException {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 5));
		
		params.put("praiseId", 4);
		
		PraiseService svc = new PraiseService(session, params);
		
		String json = svc.unlike();
		System.out.println(json);
	}
	
}
