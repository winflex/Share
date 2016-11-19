package cc.lixiaohui.share.server.config;

/**
 * 会话配置
 * @author lixiaohui
 * @date 2016年11月7日 下午11:41:32
 */
public class SessionConfig {
	
	private long timeout = 0;
	
	private long messageTimeout = 20 * 1000;

	public long getMessageTimeout() {
		return messageTimeout;
	}

	public void setMessageTimeout(long messageTimeout) {
		this.messageTimeout = messageTimeout;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
}
