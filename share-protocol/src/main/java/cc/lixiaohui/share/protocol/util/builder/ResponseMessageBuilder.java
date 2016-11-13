package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.protocol.ResponseMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:35:14
 */
public class ResponseMessageBuilder extends MessageBuilder {
	
	/* 
	 * @see cc.lixiaohui.share.protocol.util.builder.MessageBuilder#build()
	 */
	@Override
	public ResponseMessage build() {
		return new ResponseMessage(this);
	}
	
}
