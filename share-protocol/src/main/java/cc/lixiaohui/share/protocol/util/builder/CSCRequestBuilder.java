package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.Map;

import cc.lixiaohui.share.protocol.CSCRequestMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:15:37
 */
public class CSCRequestBuilder extends RequestBuilder {
	
	protected int toUserId;
	
	protected String text;
	
	protected long requestTime;

	@Override
	public CSCRequestMessage build() {
		return new CSCRequestMessage(this);
	}
	
	public int toUserId() {
		return toUserId;
	}

	public CSCRequestBuilder toUserId(int toUserId) {
		this.toUserId = toUserId;
		return this;
	}

	public String text() {
		return text;
	}

	public CSCRequestBuilder text(String text) {
		this.text = text;
		return this;
	}

	public long requestTime() {
		return requestTime;
	}

	public CSCRequestBuilder requestTime(long requestTime) {
		this.requestTime = requestTime;
		return this;
	}

	@Override
	public CSCRequestBuilder property(String key, Serializable value) {
		return (CSCRequestBuilder) super.property(key, value);
	}

	@Override
	public CSCRequestBuilder properties(Map<String, Serializable> properties) {
		return (CSCRequestBuilder) super.properties(properties);
	}
	
}
