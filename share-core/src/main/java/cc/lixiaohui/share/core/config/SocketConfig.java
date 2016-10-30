package cc.lixiaohui.share.core.config;

/**
 * Socket配置
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午11:52:20
 */
public class SocketConfig {
	
	private int port;
	
	private String bindAddress;
	
	private int ioEventThreads;

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
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
	
}
