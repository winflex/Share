package cc.lixiaohui.share.client.util;

import net.sf.cglib.proxy.Enhancer;
import cc.lixiaohui.share.client.IShareClient;
import cc.lixiaohui.share.client.ShareClientImpl;

/**
 * 生成代理客户端
 * @author lixiaohui
 * @date 2016年11月16日 下午4:07:05
 */
public class ShareClientProxy {
	
	public static IShareClient getInstance(String host, int port) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(ShareClientImpl.class);
		ShareClientInterceptor interceptor = new ShareClientInterceptor();
		enhancer.setCallback(interceptor);
		IShareClient client = (ShareClientImpl) enhancer.create(new Class<?>[]{String.class, int.class}, new Object[]{host, port});
		return client;
	}
	
}
