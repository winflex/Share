package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.CSRequestBuilder;
import cc.lixiaohui.share.util.PropertyMap;

/**
 * 客户端->服务器请求消息, 大部分是查库请求
 * @author lixiaohui
 * @date 2016年11月7日 下午10:22:51
 */
public class CSRequestMessage extends RequestMessage {
	
	private static final long serialVersionUID = -3458168037521527779L;

	/**
	 * 调用的服务名
	 */
	private String service;
	
	/**
	 * 调用的过程名
	 */
	private String procedure;
	
	/**
	 * 参数
	 */
	private PropertyMap parameterMap = new PropertyMap();
	
	public CSRequestMessage() {}
	
	public CSRequestMessage(CSRequestBuilder builder) {
		super(builder);
		this.service = builder.service();
		this.procedure = builder.procedure();
		this.parameterMap = builder.parameters();
	}
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public PropertyMap getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(PropertyMap parameters) {
		this.parameterMap = parameters;
	}
	
	public static CSRequestBuilder builder() {
		return new CSRequestBuilder();
	}
	
	@Override
	public String toString() {
		return String.format("CSRequestMessage[id=%s, s=%s, p=%s, args=%s]", id, service, procedure, properties);
	}
}
