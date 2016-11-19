package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.HandShakeRequestBuilder;

/**
 * 连接建立时, 服务端主动给客户端发的消息, 包含连接的相关参数
 * 
 * @author lixiaohui
 * @date 2016年11月8日 下午10:34:03
 */
public class HandShakeRequestMessage extends Message {

	private static final long serialVersionUID = 6144729504636857321L;

	private long requestTimeout;
	
	private long heartbeatInterval;
	
	private int reconnectTimes;
	
	private long reconnectInterval;
	

	public HandShakeRequestMessage(){}
	
	public HandShakeRequestMessage(HandShakeRequestBuilder builder) {
		super(builder);
		heartbeatInterval = builder.heartbeatInterval();
		requestTimeout = builder.requestTimeout();
		reconnectInterval = builder.reconnectInterval();
		reconnectTimes = builder.reconnectTimes();
	}

	public long getHeartbeatInterval() {
		return heartbeatInterval;
	}

	public long getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(long requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	public void setHeartbeatInterval(long heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}

	
	public int getReconnectTimes() {
		return reconnectTimes;
	}

	public void setReconnectTimes(int reconnectTimes) {
		this.reconnectTimes = reconnectTimes;
	}

	public long getReconnectInterval() {
		return reconnectInterval;
	}

	public void setReconnectInterval(long reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

	public static HandShakeRequestBuilder builder() {
		return new HandShakeRequestBuilder();
	}

}
