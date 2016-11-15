package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.PushMessageBuilder;

/**
 * 服务器 -> 客户端 推送消息
 * @author lixiaohui
 * @date 2016年11月7日 下午11:03:48
 */
public class PushMessage extends Message {
	
	private static final long serialVersionUID = 6813095621122249172L;
	
	private String pushData;
	
	public PushMessage() {}
	
	public PushMessage(PushMessageBuilder builder) {
		super(builder);
		this.pushData = builder.pushData();
	}
	
	/**
	 * @return the pushData
	 */
	public String getPushData() {
		return pushData;
	}

	/**
	 * @param pushData the pushData to set
	 */
	public void setPushData(String pushData) {
		this.pushData = pushData;
	}
	
	public static PushMessageBuilder builder() {
		return new PushMessageBuilder();
	}
}
