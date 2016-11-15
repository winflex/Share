package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;

import cc.lixiaohui.share.protocol.CSResponseMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:26:45
 */
public class CSResponseBuilder extends ResponseBuilder {

	@Override
	public CSResponseMessage build() {
		return new CSResponseMessage(this);
	}
	
	@Override
	public CSResponseBuilder correlationId(long id) {
		return (CSResponseBuilder) super.correlationId(id);
	}

	@Override
	public CSResponseBuilder responseJson(String respJson) {
		return (CSResponseBuilder) super.responseJson(respJson);
	}

	@Override
	public CSResponseBuilder property(String key, Serializable value) {
		return (CSResponseBuilder) super.property(key, value);
	}
	
	
	
}
