package cc.lixiaohui.share.protocol.util.builder;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:15:37
 */
public class CSCRequestMessageBuilder extends RequestMessageBuilder {
	
	protected int toUserId;
	
	protected String text;
	
	protected long requestTime;

	public int toUserId() {
		return toUserId;
	}

	public CSCRequestMessageBuilder toUserId(int toUserId) {
		this.toUserId = toUserId;
		return this;
	}

	public String text() {
		return text;
	}

	public CSCRequestMessageBuilder text(String text) {
		this.text = text;
		return this;
	}

	public long requestTime() {
		return requestTime;
	}

	public CSCRequestMessageBuilder requestTime(long requestTime) {
		this.requestTime = requestTime;
		return this;
	}
}
