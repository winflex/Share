package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.core.Session;
import cc.lixiaohui.share.server.core.SystemRuntime;
import cc.lixiaohui.share.server.service.util.ServiceException;

/**
 * @author lixiaohui
 * @date 2016年11月19日 下午7:36:03
 */
public class CollectionServiceTest {
	
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	@Test
	public void getUserCollections() throws ServiceException {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 4));
		CollectionService svc = new CollectionService(session, params);
		String json = svc.getUserCollection();
		System.out.println(json);
	}
	
	@Test
	public void getShareCollections() throws ServiceException {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 4));
		CollectionService svc = new CollectionService(session, params);
		String json = svc.getShareCollection();
		System.out.println(json);
	}
	
	@Test
	public void uncollectShare() throws ServiceException {
		params.put("collectionId", 2);
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 4));
		CollectionService svc = new CollectionService(session, params);
		String json = svc.unCollectShare();
		System.out.println(json);
	}
	
	@Test
	public void uncollectUser() throws ServiceException {
		params.put("collectionId", 2);
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 4));
		CollectionService svc = new CollectionService(session, params);
		String json = svc.unCollectUser();
		System.out.println(json);
	}
}
