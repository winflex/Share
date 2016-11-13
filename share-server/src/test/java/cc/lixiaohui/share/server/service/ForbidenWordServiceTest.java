package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.SystemRuntime;

/**
 * @author lixiaohui
 * @date 2016年11月12日 上午12:23:11
 */
public class ForbidenWordServiceTest {
	
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	@Test
	public void test() {
		/*ForbidenWordService svc = new ForbidenWordService(null, null);
		int id = 1;
		String j1 = svc.getForbidenWords(0, 10);
		String j2 = svc.deleteForbidenWord(id);
		String j3 = svc.getForbidenWords(0, 10);
		String j4 = svc.recoverForbidenWord(id);
		String j5 = svc.getForbidenWords(0, 10);
		System.out.println(j1);
		System.out.println(j2);
		System.out.println(j3);
		System.out.println(j4);
		System.out.println(j5);*/
	}
	@Test
	public void listSomeDeleted() {
		params.put("start", 0);
		params.put("limit", 10);
		ForbidenWordService svc = new ForbidenWordService(null, params);
		String j1 = svc.getDeletedForbidenWords();
		System.out.println(j1);
	}
	
	@Test
	public void delete() {
		params.put("id", 3);
		ForbidenWordService svc = new ForbidenWordService(null, params);
		String json = svc.deleteForbidenWord();
		System.out.println(json);
	}
	
}
