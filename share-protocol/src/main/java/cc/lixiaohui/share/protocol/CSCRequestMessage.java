package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.CSCRequestBuilder;

/**
 * 客户端 -> 服务器 -> 客户端转发消息请求
 * @author lixiaohui
 * @date 2016年11月7日 下午10:21:27
 */
public class CSCRequestMessage extends RequestMessage {
	
	private static final long serialVersionUID = 7284996519467938957L;

	/** 目标用户ID */
	private int toUserId;
	
	/** 源用户ID, 由服务器中转时设置 */
	private int fromUserId;
	
	/** 文本消息  */
	private String text;
	
	/** 消息从客户端发出时间 */
	private long requestTime;

	
	public CSCRequestMessage(){}
	
	public CSCRequestMessage(CSCRequestBuilder builder) {
		super(builder);
		this.toUserId = builder.toUserId();
		this.text = builder.text();
		this.requestTime = builder.requestTime();
		this.fromUserId = builder.fromUserId();
	}
	
	/**
	 * @return the toUserId
	 */
	public int getToUserId() {
		return toUserId;
	}

	/**
	 * @param toUserId the toUserId to set
	 */
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the requestTime
	 */
	public long getRequestTime() {
		return requestTime;
	}

	/**
	 * @param requestTime the requestTime to set
	 */
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}
	
	public static CSCRequestBuilder builder() {
		return new CSCRequestBuilder();
	}
}
