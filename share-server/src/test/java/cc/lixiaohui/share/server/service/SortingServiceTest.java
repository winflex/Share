package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.SystemRuntime;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:45:37
 */
public class SortingServiceTest {
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	@Test
	public void test() throws ServiceException {
		params.put("sortingId", 5);
		SortingService svc = new SortingService(null, params);
		System.out.println(svc.deleteSorting());
	}
	
}
