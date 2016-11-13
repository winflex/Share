package cc.lixiaohui.share.client;

/**
 * 客户端参数配置, 支持链式调用
 * @author lixiaohui
 * @date 2016年10月31日 下午4:22:39
 */
public interface IConfiguration {

	/**
	 * 获取服务器host
	 */
	String host();

	/**
	 * 设置服务器host
	 * @param host
	 * @return this
	 */
	IConfiguration host(String host);
	
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
	IConfiguration port(int port);

	/**
	 * 设置心跳周期
	 * @param milliseconds 心跳周期 in milliseconds
	 * @return this
	 */
	IConfiguration heartbeanInterval(long heartbeatInterval);
	
	/**
	 * 获取心跳周期
	 * @return 心跳周期
	 */
	long heartbeatInterval();
	
	/**
	 * 当客户端由于某中原因与服务器端口连接时, 该值指定重连次数, 0为不重连, 负数为无限重连
	 * @return the reconnectTimes
	 */
	int reconnectTimes();
	
	/**
	 * 当客户端由于某中原因与服务器端口连接时, 该值指定重连次数, 0为不重连, 负数为无限重连
	 * @param times 次数
	 * @return this
	 */
	IConfiguration reconnectTimes(int times);
	
	/**
	 * 重连间隔
	 * @return 重连间隔, 毫秒
	 */
	long reconnectInterval();
	
	/**
	 * 重连间隔
	 * @param interval 重连间隔, 毫秒
	 * @return this
	 */
	IConfiguration reconnectInterval(long interval);
	
	/**
	 * 获取线程数
	 * @return 线程数
	 */
	int ioThreads();
	
	/**
	 * 设置线程数
	 * @param ioThreads
	 * @return this
	 */
	IConfiguration ioThreads(int ioThreads);
	
	/**
	 * 序列化工厂类
	 * @return
	 */
	String serializeFactoryClass();
	
	/**
	 * 序列化工厂类
	 * @return this
	 */
	IConfiguration serializeFactoryClass(String klass);

}
