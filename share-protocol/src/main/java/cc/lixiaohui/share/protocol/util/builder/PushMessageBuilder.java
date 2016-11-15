package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.Map;

import cc.lixiaohui.share.protocol.PushMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:27:23
 */
public class PushMessageBuilder extends MessageBuilder {
	
	protected String pushData;
	
	protected long pushTime;
	
	public String pushData() {
		return pushData;
	}

	public PushMessageBuilder pushData(String pushData) {
		this.pushData = pushData;
		return this;
	}
	
	public long pushTime() {
		return pushTime;
	}
	
	public PushMessageBuilder pushTime(long pushTime) {
		this.pushTime = pushTime;
		return this;
	}
	
	@Override
	public PushMessageBuilder property(String key, Serializable value) {
		return (PushMessageBuilder) super.property(key, value);
	}

	@Override
	public PushMessageBuilder properties(Map<String, Serializable> properties) {
		return (PushMessageBuilder) super.properties(properties);
	}

	@Override
	public PushMessage build() {
		return new PushMessage(this);
	}
}
