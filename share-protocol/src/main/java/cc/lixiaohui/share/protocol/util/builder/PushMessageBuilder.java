package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.protocol.PushMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:27:23
 */
public class PushMessageBuilder extends MessageBuilder {
	
	protected String pushData;
	
	public String pushData() {
		return pushData;
	}

	public PushMessageBuilder pushData(String pushData) {
		this.pushData = pushData;
		return this;
	}
	
	/* 
	 * @see cc.lixiaohui.share.protocol.util.builder.MessageBuilder#build()
	 */
	@Override
	public PushMessage build() {
		return new PushMessage(this);
	}
}
