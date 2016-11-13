package cc.lixiaohui.share.client;

import cc.lixiaohui.share.util.IBuilder;


/**
 * Client Builder
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午4:27:53
 */
public class ShareClientBuilder implements IBuilder<IShareClient>, IConfiguration {

	private String host;
	
	private int port;
	
	private long heartbeat = 5 * 1000;
	
	private int reconnectTimes = 3;
	
	private long reconnectInterval = 3 * 1000;
	
	private int ioThreads = 1;
	
	private String seiralizeFactoryClassName = "cc.lixiaohui.share.protocol.codec.serialize.factory.DefaultSerializeFactory";
	
	public ShareClientBuilder(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
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
	public int reconnectTimes() {
		return reconnectTimes;
	}

	@Override
	public IConfiguration host(String host) {
		this.host = host;
		return this;
	}

	@Override
	public IConfiguration port(int port) {
		this.port = port;
		return this;
	}

	@Override
	public IConfiguration reconnectTimes(int times) {
		this.reconnectTimes = times;
		return this;
	}

	@Override
	public IConfiguration heartbeanInterval(long milliseconds) {
		this.heartbeat = milliseconds;
		return this;
	}

	@Override
	public long heartbeatInterval() {
		return heartbeat;
	}

	@Override
	public int ioThreads() {
		return ioThreads;
	}
	
	@Override
	public IConfiguration ioThreads(int ioThreads) {
		this.ioThreads = ioThreads;
		return this;
	}

	/* 
	 * @see cc.lixiaohui.share.client.Configuration#reconnectInterval()
	 */
	@Override
	public long reconnectInterval() {
		return reconnectInterval;
	}

	/* 
	 * @see cc.lixiaohui.share.client.Configuration#reconnectInterval(long)
	 */
	@Override
	public IConfiguration reconnectInterval(long interval) {
		this.reconnectInterval = interval;
		return this;
	}

	@Override
	public String serializeFactoryClass() {
		return seiralizeFactoryClassName;
	}

	@Override
	public IConfiguration serializeFactoryClass(String klassName) {
		this.seiralizeFactoryClassName = klassName;
		return this;
	}
}
