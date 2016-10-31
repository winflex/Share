package cc.lixiaohui.share.client;

/**
 * 客户端参数配置
 * @author lixiaohui
 * @date 2016年10月31日 下午4:22:39
 */
public interface Configuration {

	/**
	 * 获取服务器host
	 */
	String host();

	/**
	 * 设置服务器host
	 * @param host
	 * @return this
	 */
	Configuration host(String host);
	
	/**
	 * @return 服务器端口
	 */
	int port();
	
	/**
	 * 
	 * 设置服务器端口
	 * @param port 端口
	 * @return this
	 */
	Configuration port(int port);

	/**
	 * 当客户端初始化时连不上服务器, 该值指定重连次数
	 * @return the retryTimesOnStartUpFailed
	 */
	int retryTimesOnStartUpFailed();
	
	/**
	 * 当客户端初始化时连不上服务器, 该值指定重连次数
	 * @param times 次数
	 * @return this
	 */
	Configuration retryTimesOnStartUpFailed(int times);
	
	/**
	 * 当客户端由于某中原因与服务器端口连接时, 该值指定重连次数
	 * @return the retryTimesOnConnectionClosed
	 */
	int retryTimesOnConnectionClosed();
	
	/**
	 * 当客户端由于某中原因与服务器端口连接时, 该值指定重连次数
	 * @param times
	 * @return this
	 */
	Configuration retryTimesOnConnectionClosed(int times);

}
