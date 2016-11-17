package cc.lixiaohui.share.client;

/**
 * 连接监听器
 * @author lixiaohui
 * @date 2016年11月16日 下午7:25:28
 */
public interface IConnectionListener {
	
	void onConnected();
	
	void onClosed(Throwable cause);
	
}
