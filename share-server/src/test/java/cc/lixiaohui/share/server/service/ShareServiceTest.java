package cc.lixiaohui.share.server.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.util.HibernateSessionFactory;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
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
		//params.put("keyword", "大家");
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 10, 18, 1, 1, 1);
		//long millis = cal.getTimeInMillis();
		//long millis = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 5;
		
		//params.put("baseTime", millis);
		//params.put("userId", 11);
		
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 7));
		//Session session = TestUtils.unloginSession();
		
		ShareService svc = new ShareService(session, params);
		
		String json = svc.getShares();
		
		System.out.println(json);
		System.out.println(cal.getTime().toLocaleString());
	}
	
	
	@SuppressWarnings({ "unchecked", "unused", "deprecation" })
	@Test
	public void tt() throws ServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 11, 15, 1, 1, 1);
		long millis = cal.getTimeInMillis();
		
		org.hibernate.classic.Session session = HibernateSessionFactory.getSessionFactory().openSession();
		List<Share> shares = session.createSQLQuery("select s.* from share s where s.create_time > ':time'").addEntity("s", Share.class)
				.setParameter("time", "2016-11-15 00:00:00").list();
	
		System.out.println(cal.getTime().toLocaleString());
	}
	
	@Test
	public void getCommentedShares() throws Exception {
		Session session = TestUtils.loginSession(2, 5);
		
		params.put("start", 0);
		params.put("limit", 20);
		ShareService svc = new ShareService(session, params);
		
		String json = svc.getCommentedShares();
		System.out.println(json);
		
	}
	
	@Test
	public void getLikedShares() throws Exception {
		Session session = TestUtils.loginSession(2, 6);
		
		params.put("start", 0);
		params.put("limit", 20);
		ShareService svc = new ShareService(session, params);
		
		String json = svc.getLikedShares();
		System.out.println(json);
		
	}
	
	
}
