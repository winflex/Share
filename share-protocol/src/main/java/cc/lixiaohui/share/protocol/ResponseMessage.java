package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.ResponseBuilder;

/**
 * 响应消息基类
 * @author lixiaohui
 * @date 2016年11月7日 下午10:06:02
 */
public class ResponseMessage extends Message {
	
	private static final long serialVersionUID = 672350874252410119L;

	protected long correlationId;
	
	protected String respJson;

	public ResponseMessage() {}
	
	/**
	 * @param builder
	 */
	public ResponseMessage(ResponseBuilder builder) {
		super(builder);
		this.correlationId = builder.correlationId();
		this.respJson = builder.responseJson();
	}
	
	/**
	 * @return the correlationId
	 */
	public long getCorrelationId() {
		return correlationId;
	}

	/**
	 * @param correlationId the correlationId to set
	 */
	public void setCorrelationId(long correlationId) {
		this.correlationId = correlationId;
	}

	/**
	 * @return the respJson
	 */
	public String getRespJson() {
		return respJson;
	}

	/**
	 * @param respJson the respJson to set
	 */
	public void setRespJson(String respJson) {
		this.respJson = respJson;
	}
	
	public static ResponseBuilder builder() {
		return new ResponseBuilder();
	}
	
}
