package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.ResponseMessageBuilder;

/**
 * 响应消息基类
 * @author lixiaohui
 * @date 2016年11月7日 下午10:06:02
 */
public class ResponseMessage extends Message {
	
	/**
	 * @param builder
	 */
	public ResponseMessage(ResponseMessageBuilder builder) {
		super(builder);
	}

	private static final long serialVersionUID = 672350874252410119L;

	protected long correlationId;
	
	protected String respJson;

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
	
	public static ResponseMessageBuilder builder() {
		return new ResponseMessageBuilder();
	}
}
