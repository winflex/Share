package cc.lixiaohui.share.client;

import org.junit.Test;

import cc.lixiaohui.share.client.util.ShareClientProxy;

/**
 * @author lixiaohui
 * @date 2016年11月16日 下午7:44:49
 */
public class ClientTest {
	
	@Test
	public void connect() throws Exception {
		IShareClient client = ShareClientProxy.getInstance("localhost", 8888);
		client.start();
		System.in.read();
	}
	
}
