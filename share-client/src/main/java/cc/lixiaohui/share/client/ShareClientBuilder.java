package cc.lixiaohui.share.client;


/**
 * Client Builder
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午4:27:53
 */
public class ShareClientBuilder implements IBuilder<IShareClient>, Configuration {

	private String host;
	
	private int port;
	
	private int retryTimesOnStartUpFailed;
	
	private int retryTimesOnConnectionClosed;
	
	@Override
	public IShareClient build() {
		return new ShareClientImpl(this);
	}

	@Override
	public String host() {
		return host;
	}

	@Override
	public int port() {
		return port;
	}

	@Override
	public int retryTimesOnStartUpFailed() {
		return retryTimesOnStartUpFailed;
	}

	@Override
	public int retryTimesOnConnectionClosed() {
		return retryTimesOnConnectionClosed;
	}

	@Override
	public Configuration host(String host) {
		this.host = host;
		return this;
	}

	@Override
	public Configuration port(int port) {
		this.port = port;
		return this;
	}

	@Override
	public Configuration retryTimesOnStartUpFailed(int times) {
		this.retryTimesOnStartUpFailed = times;
		return this;
	}

	@Override
	public Configuration retryTimesOnConnectionClosed(int times) {
		this.retryTimesOnConnectionClosed = times;
		return this;
	}

}
