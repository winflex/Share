package cc.lixiaohui.share.core.config;

/**
 * @author lixiaohui
 * @date 2016年11月8日 下午11:22:55
 */
public class PoolConfig {
	
	private int corePoolsize = 5;
	
	private int maxPoolSize = 5;
	
	private long keepAliveTime = 30 * 1000;
	
	private String blockingQueue = "java.util.concurrent.LinkedBlockingQueue";

	/**
	 * @return the corePoolsize
	 */
	public int getCorePoolsize() {
		return corePoolsize;
	}

	/**
	 * @param corePoolsize the corePoolsize to set
	 */
	public void setCorePoolsize(int corePoolsize) {
		this.corePoolsize = corePoolsize;
	}

	/**
	 * @return the maxPoolSize
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * @param maxPoolSize the maxPoolSize to set
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return the keepAliveTime
	 */
	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	/**
	 * @param keepAliveTime the keepAliveTime to set
	 */
	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public String getBlockingQueue() {
		return blockingQueue;
	}

	public void setBlockingQueue(String blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
	
}
