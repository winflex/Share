package cc.lixiaohui.share.server.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.util.TimeUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
		Session session = Session.builder().build();
		session.login(11, "李酷酷", false, false);
		
		params.put("content", "不好");
		params.put("pictureIds", new int[]{1, 2});
		
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
		params.put("shareId", 9);
		
		ShareService svc = new ShareService(null, params);
		String json = svc.getShare();
		System.out.println(json);
	}
	
	@Test
	public void getShares() throws ServiceException {
		params.put("keyword", "大家");
		
		ShareService svc = new ShareService(null, params);
		
		String json = svc.getShares();
		System.out.println(json);
	}
	
	public static void main(String[] args) {
		JSONObject result = new JSONObject();
		result.put("a", new Timestamp(TimeUtils.currentTimeMillis()));
		System.out.println(JSON.toJSONString(result));
	}
}
