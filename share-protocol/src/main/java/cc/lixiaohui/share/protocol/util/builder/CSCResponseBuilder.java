package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;

import cc.lixiaohui.share.protocol.CSCResponseMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:18:51
 */
public class CSCResponseBuilder extends ResponseBuilder {

	protected long responseTime;
	
	public long responseTime() {
		return responseTime;
	}
	
	public CSCResponseBuilder responseTime(long responseTime) {
		this.responseTime = responseTime;
		return this;
	}

	@Override
	public CSCResponseBuilder correlationId(long id) {
		return (CSCResponseBuilder) super.correlationId(id);
	}

	@Override
	public CSCResponseBuilder responseJson(String respJson) {
		return (CSCResponseBuilder) super.responseJson(respJson);
	}

	@Override
	public CSCResponseBuilder property(String key, Serializable value) {
		return (CSCResponseBuilder) super.property(key, value);
	}

	@Override
	public CSCResponseMessage build() {
		return new CSCResponseMessage(this);
	}
	
}
