package cc.lixiaohui.share.client;

import java.io.File;
import java.util.concurrent.Executors;

import org.junit.Test;

import cc.lixiaohui.share.client.util.storage.LocalStorage;

/**
 * @author lixiaohui
 * @date 2016年11月23日 下午10:22:19
 */
public class StorageTest {
	
	@Test
	public void test() throws Exception {
		LocalStorage ls = LocalStorage.open(System.getProperty("user.dir") + File.separator + "pictures", Executors.newSingleThreadExecutor());
		
		byte[] b = ls.getPicture(1);
		assert b == null;
		
		ls.store(1, "我".getBytes());
		b = ls.getPicture(1);
		System.out.println(new String(b));
		ls.close();
	}
	
}
