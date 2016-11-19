package cc.lixiaohui.share.client;

import org.junit.Test;

import cc.lixiaohui.share.client.util.ClientException;

/**
 * @author lixiaohui
 * @date 2016年11月16日 下午7:44:49
 */
public class ClientTest {
	
	@Test
	public void connect() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("localhost", 8888);
		client.start();
		
		/*String json = client.register("卧槽", "wocao");
		System.out.println(json);*/
		
		String json1 = client.login("李小辉", "lixiaohui");
		System.out.println(json1);
		
		String json2 = client.getShares(null, 0, 0, 0, 20, false);
		System.out.println(json2);
		System.in.read();
	}
	
}
