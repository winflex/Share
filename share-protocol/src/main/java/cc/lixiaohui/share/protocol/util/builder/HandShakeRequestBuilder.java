package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.Map;

import cc.lixiaohui.share.protocol.HandShakeRequestMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:44:22
 */
public class HandShakeRequestBuilder extends MessageBuilder {
	
	private long requestTimeout;
	
	private long heartbeatInterval;
	
	private int reconnectTimes;
	
	private long reconnectInterval;
	
	public long heartbeatInterval() {
		return heartbeatInterval;
	}

	public HandShakeRequestBuilder heartbeatInterval(long heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
		return this;
	}
	
	public long reconnectInterval() {
		return reconnectInterval;
	}
	
	public HandShakeRequestBuilder reconnectInterval(long reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
		return this;
	}
	
	public int reconnectTimes() {
		return reconnectTimes;
	}
	
	public long requestTimeout() {
		return requestTimeout;
	}

	public HandShakeRequestBuilder requestTimeout(long requestTimeout) {
		this.requestTimeout = requestTimeout;
		return this;
	}

	public HandShakeRequestBuilder reconnectTimes(int reconnectTimes) {
		this.reconnectTimes = reconnectTimes;
		return this;
	}
	
	@Override
	public HandShakeRequestMessage build() {
		return new HandShakeRequestMessage(this);
	}

	@Override
	public HandShakeRequestBuilder property(String key, Serializable value) {
		return (HandShakeRequestBuilder) super.property(key, value);
	}


	@Override
	public HandShakeRequestBuilder properties(Map<String, Serializable> properties) {
		return (HandShakeRequestBuilder) super.properties(properties);
	}
	
	
}
