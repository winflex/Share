package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.util.PropertyMap;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:20:25
 */
public class CSRequestMessageBuilder extends RequestMessageBuilder {
	
	protected String serviceName;
	
	protected String procedureName;
	
	protected PropertyMap parameters = new PropertyMap();

	public String serviceName() {
		return serviceName;
	}

	public CSRequestMessageBuilder serviceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	public String procedureName() {
		return procedureName;
	}

	public CSRequestMessageBuilder procedureName(String procedureName) {
		this.procedureName = procedureName;
		return this;
	}

	public PropertyMap parameters() {
		return parameters;
	}

	public CSRequestMessageBuilder parameters(PropertyMap parameters) {
		this.parameters = parameters;
		return this;
	}
	
	public String parameter(String key) {
		return parameters.get(key);
	}
	
	public CSRequestMessageBuilder parameter(String key, String value) {
		parameters.put(key, value);
		return this;
	}
}
