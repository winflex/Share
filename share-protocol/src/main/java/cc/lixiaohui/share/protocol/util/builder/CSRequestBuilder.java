package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.Map;

import cc.lixiaohui.share.protocol.CSRequestMessage;
import cc.lixiaohui.share.util.PropertyMap;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:20:25
 */
public class CSRequestBuilder extends RequestBuilder {
	
	protected String service;
	
	protected String procedure;
	
	protected PropertyMap parameters = new PropertyMap();

	@Override
	public CSRequestMessage build() {
		return new CSRequestMessage(this);
	}
	
	public String service() {
		return service;
	}

	public CSRequestBuilder service(String serviceName) {
		this.service = serviceName;
		return this;
	}

	public String procedure() {
		return procedure;
	}

	public CSRequestBuilder procedure(String procedureName) {
		this.procedure = procedureName;
		return this;
	}

	public PropertyMap parameters() {
		return parameters;
	}

	public CSRequestBuilder parameters(PropertyMap parameters) {
		this.parameters = parameters;
		return this;
	}
	
	public String parameter(String key) {
		return String.valueOf(parameters.get(key));
	}
	
	public CSRequestBuilder parameter(String key, String value) {
		parameters.put(key, value);
		return this;
	}

	@Override
	public CSRequestBuilder property(String key, Serializable value) {
		return (CSRequestBuilder) super.property(key, value);
	}

	@Override
	public CSRequestBuilder properties(Map<String, Serializable> properties) {
		return (CSRequestBuilder) super.properties(properties);
	}
	
}
