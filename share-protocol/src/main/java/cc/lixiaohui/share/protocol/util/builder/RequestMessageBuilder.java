package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.protocol.RequestMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:34:51
 */
public class RequestMessageBuilder extends MessageBuilder {
	
	@Override
	public RequestMessage build() {
		return new RequestMessage(this);
	}
	
}
