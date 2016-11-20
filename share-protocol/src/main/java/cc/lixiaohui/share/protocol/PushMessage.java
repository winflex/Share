package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.PushMessageBuilder;

/**
 * 服务器 -> 客户端 推送消息
 * @author lixiaohui
 * @date 2016年11月7日 下午11:03:48
 */
public class PushMessage extends Message {
	
	public static enum Type{
		
		/** 好友添加请求 */
		FRI_REQ,
		
		/** 好友添加答复 */
		FRI_RESP,
		
		/** 好友删除 */
		FRI_DEL,
		
		/** 新的分享 */
		SHARE,
			
		/** 新的评论 */
		COMMENT,
		
		/** 新的点赞*/
		LIKE,
		
		/** 取消点赞 */
		UNLIKE,
	}
	
	private static final long serialVersionUID = 6813095621122249172L;
	
	private Type type;
	
	private String pushData;
	
	public PushMessage() {}
	
	public PushMessage(PushMessageBuilder builder) {
		super(builder);
		this.pushData = builder.pushData();
		this.type = builder.type();
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPushData() {
		return pushData;
	}

	public void setPushData(String pushData) {
		this.pushData = pushData;
	}
	
	public static PushMessageBuilder builder() {
		return new PushMessageBuilder();
	}
	
}
