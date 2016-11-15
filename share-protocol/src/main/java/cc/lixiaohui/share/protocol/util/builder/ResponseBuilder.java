package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.Map;

import cc.lixiaohui.share.protocol.ResponseMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:35:14
 */
public class ResponseBuilder extends MessageBuilder {
	
	protected long correlationId;
	
	protected String respJson;
	
	@Override
	public ResponseMessage build() {
		return new ResponseMessage(this);
	}
	
	public long correlationId() {
		return correlationId;
	}
	
	public ResponseBuilder correlationId(long id) {
		this.correlationId = id;
		return this;
	}
	
	public String responseJson() {
		return respJson;
	}
	
	public ResponseBuilder responseJson(String respJson) {
		this.respJson = respJson;
		return this;
	}
	
	@Override
	public ResponseBuilder properties(Map<String, Serializable> properties) {
		return (ResponseBuilder) super.properties(properties);
	}
	
	@Override
	public ResponseBuilder property(String key, Serializable value) {
		return (ResponseBuilder) super.property(key, value);
	}
}
