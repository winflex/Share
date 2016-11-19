package cc.lixiaohui.share.client;

import org.junit.Test;

import cc.lixiaohui.share.client.util.ClientException;
import cc.lixiaohui.share.client.util.ShareClientProxy;

/**
 * @author lixiaohui
 * @date 2016年11月16日 下午7:44:49
 */
public class ClientTest {
	
	@Test
	public void connect() throws Exception {
		final IShareClient client = ShareClientProxy.getInstance("localhost", 8888);
		client.start();
		
		String json = client.register("卧槽", "wocao");
		System.out.println(json);
		
		System.in.read();
	}
	
}
