package cc.lixiaohui.share.client;

import org.junit.Test;

/**
 * @author lixiaohui
 * @date 2016年11月16日 下午7:44:49
 */
public class ClientTest {
	
	@Test
	public void connect() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("172.16.28.88", 8888);
		client.start();
		
		String json = client.register("sesae", "se");
		System.out.println(json);
		
		/*String json1 = client.login("李小辉", "lixiaohui");
		System.out.println(json1);*/
		
	//	String json2 = client.getShare(10);
		//System.out.println(json2);
		System.in.read();
	}
	
}
