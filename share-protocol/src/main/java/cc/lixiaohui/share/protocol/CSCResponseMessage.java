package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.CSCResponseBuilder;

/**
 * 客户端 -> 服务器 -> 客户端消息响应
 * @author lixiaohui
 * @date 2016年11月7日 下午10:22:21
 */
public class CSCResponseMessage extends ResponseMessage {

	private static final long serialVersionUID = 6253057586986256971L;
	
	/** 目标客户端发出该响应的时间 */
	private long responseTime;

	
	public CSCResponseMessage() {}

	public CSCResponseMessage(CSCResponseBuilder builder) {
		super(builder);
		this.responseTime = builder.responseTime();
	}
	
	/**
	 * @return the responseTime
	 */
	public long getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	
	public static CSCResponseBuilder builder() {
		return new CSCResponseBuilder();
	}
}
