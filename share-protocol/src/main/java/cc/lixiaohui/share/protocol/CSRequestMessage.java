package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.CSRequestMessageBuilder;
import cc.lixiaohui.share.util.PropertyMap;

/**
 * 客户端->服务器请求消息, 大部分是查库请求
 * @author lixiaohui
 * @date 2016年11月7日 下午10:22:51
 */
public class CSRequestMessage extends RequestMessage {
	
	private static final long serialVersionUID = -3458168037521527779L;

	public CSRequestMessage(CSRequestMessageBuilder builder) {
		super(builder);
	}
	
	/**
	 * 调用的服务名
	 */
	private String serviceName;
	
	/**
	 * 调用的过程名
	 */
	private String procedureName;
	
	/**
	 * 参数
	 */
	private PropertyMap parameters = new PropertyMap();
	

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the procedureName
	 */
	public String getProcedureName() {
		return procedureName;
	}
	/**
	 * @param procedureName the procedureName to set
	 */
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	/**
	 * @return the parameters
	 */
	public PropertyMap getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(PropertyMap parameters) {
		this.parameters = parameters;
	}
	
	public static CSRequestMessageBuilder builder() {
		return new CSRequestMessageBuilder();
	}
}
