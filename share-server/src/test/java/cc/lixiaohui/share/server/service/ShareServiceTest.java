package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

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
	public void getShares() throws ServiceException {
		Session session = Session.builder().build();
		
		params.put("keyword", "今天");
		
		ShareService svc = new ShareService(session, params);
		String json = svc.getShares();
		System.out.println(json);
		
	}
	
}
