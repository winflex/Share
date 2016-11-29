package cc.lixiaohui.share.server.core.config;

/**
 * Socket配置
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午11:52:20
 */
public class SocketConfig {
	
	private int port = DEFAULT_BIND_PORT;
	
	private String bindAddress = DEFAULT_BIND_ADDRESS;
	
	private int ioEventThreads = DEFAULT_IO_THREADS;
	
	private int acceptorThreads = 1;
	
	private long heartbeatInterval = DEFAULT_HEARTBEAT_INTERVAL;
	
	private int maxHeartbeatMissTimes = MAX_HEARTBEAT_MISSTIMES;
	
	private long reconnectInterval = 1000;
	
	private int reconnectTimes = 5;
	
	private String serializeFactoryClass = DEFAULT_SERIALIZE_FACTORY;

	

	public static final String DEFAULT_BIND_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_BIND_PORT = 9999;
	public static final int DEFAULT_HEARTBEAT_INTERVAL = 10 * 1000; //ms
	public static final int MAX_HEARTBEAT_MISSTIMES = 10; //ms
	public static final int DEFAULT_IO_THREADS = Runtime.getRuntime().availableProcessors() + 1;
	public static final String DEFAULT_SERIALIZE_FACTORY = "cc.lixiaohui.share.protocol.codec.serialize.factory.HessianSerializeFactory";
	
	/**
	 * @return the serializeFactoryClass
	 */
	public String getSerializeFactoryClass() {
		return serializeFactoryClass;
	}

	/**
	 * @param serializeFactoryClass the serializeFactoryClass to set
	 */
	public void setSerializeFactoryClass(String serializeFactoryClass) {
		this.serializeFactoryClass = serializeFactoryClass;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	public long getReconnectInterval() {
		return reconnectInterval;
	}

	public void setReconnectInterval(long reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

	public int getReconnectTimes() {
		return reconnectTimes;
	}

	public void setReconnectTimes(int reconnectTimes) {
		this.reconnectTimes = reconnectTimes;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the bindAddress
	 */
	public String getBindAddress() {
		return bindAddress;
	}

	/**
	 * @param bindAddress the bindAddress to set
	 */
	public void setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
	}

	/**
	 * @return the ioEventThreads
	 */
	public int getIoEventThreads() {
		return ioEventThreads;
	}

	/**
	 * @param ioEventThreads the ioEventThreads to set
	 */
	public void setIoEventThreads(int ioEventThreads) {
		this.ioEventThreads = ioEventThreads;
	}

	/**
	 * @return the acceptorThreads
	 */
	public int getAcceptorThreads() {
		return acceptorThreads;
	}

	/**
	 * @param acceptorThreads the acceptorThreads to set
	 */
	public void setAcceptorThreads(int acceptorThreads) {
		this.acceptorThreads = acceptorThreads;
	}

	/**
	 * @return the heartbeatInterval
	 */
	public long getHeartbeatInterval() {
		return heartbeatInterval;
	}

	/**
	 * @param heartbeatInterval the heartbeatInterval to set
	 */
	public void setHeartbeatInterval(long heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}

	/**
	 * @return the maxHeartbeatMissTimes
	 */
	public int getMaxHeartbeatMissTimes() {
		return maxHeartbeatMissTimes;
	}

	/**
	 * @param maxHeartbeatMissTimes the maxHeartbeatMissTimes to set
	 */
	public void setMaxHeartbeatMissTimes(int maxHeartbeatMissTimes) {
		this.maxHeartbeatMissTimes = maxHeartbeatMissTimes;
	}
	
}
