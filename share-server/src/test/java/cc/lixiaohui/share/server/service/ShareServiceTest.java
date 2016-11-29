package cc.lixiaohui.share.server.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
import cc.lixiaohui.share.server.model.bean.Share;
import cc.lixiaohui.share.server.model.util.HibernateSessionFactory;
import cc.lixiaohui.share.server.service.util.ServiceException;

/**
 * @author lixiaohui
 * @date 2016年11月13日 下午10:14:09
 */
public class ShareServiceTest {
	
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	
	@Test
	public void publishShare() throws ServiceException {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 11));
		params.put("content", "fdafdsfdsdsfdfsss");
		params.put("pictureIds", new int[]{3,4});
		ShareService svc = new ShareService(session, params);
		String json = svc.publishShare();
		System.out.println(json);
	}
	
	@Test
	public void delete() throws ServiceException {
		params.put("shareId", 9);
		params.put("physically", true);
		ShareService svc = new ShareService(null, params);
		String json = svc.deleteShare();
		System.out.println(json);
	}
	
	@Test
	public void getShare() throws ServiceException {
		params.put("shareId", 10);
		ShareService svc = new ShareService(null, params);
		String json = svc.getShare();
		System.out.println(json);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void getShares() throws ServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 10, 18, 1, 1, 1);
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		ShareService svc = new ShareService(session, params);
		String json = svc.getShares();
	}
	@Test
	public void getCommentedShares() throws Exception {
		Session session = TestUtils.loginSession(2, 5);
		params.put("start", 0);
		params.put("limit", 20);
		ShareService svc = new ShareService(session, params);
		String json = svc.getCommentedShares();
	}
	@Test
	public void getLikedShares() throws Exception {
		Session session = TestUtils.loginSession(2, 6);
		params.put("start", 0);
		params.put("limit", 20);
		ShareService svc = new ShareService(session, params);
		String json = svc.getLikedShares();
	}
	
	
}
