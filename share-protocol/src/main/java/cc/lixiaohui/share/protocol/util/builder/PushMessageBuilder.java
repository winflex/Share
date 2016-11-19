package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.Map;

import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.protocol.PushMessage.Type;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:27:23
 */
public class PushMessageBuilder extends MessageBuilder {
	
	protected Type type;
	
	protected String pushData;
	
	public String pushData() {
		return pushData;
	}

	public PushMessageBuilder pushData(String pushData) {
		this.pushData = pushData;
		return this;
	}
	
	public Type type() {
		return type;
	}

	public PushMessageBuilder type(Type type) {
		this.type = type;
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
