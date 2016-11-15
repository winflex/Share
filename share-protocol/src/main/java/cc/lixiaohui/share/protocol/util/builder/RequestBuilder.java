package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.Map;

import cc.lixiaohui.share.protocol.RequestMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:34:51
 */
public class RequestBuilder extends MessageBuilder {
	
	@Override
	public RequestMessage build() {
		return new RequestMessage(this);
	}

	@Override
	public RequestBuilder property(String key, Serializable value) {
		return (RequestBuilder) super.property(key, value);
	}

	@Override
	public RequestBuilder properties(Map<String, Serializable> properties) {
		return (RequestBuilder) super.properties(properties);
	}
	
	
}
