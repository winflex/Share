package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.protocol.HandshakeResponseMessage;

/**
 * @author lixiaohui
 * @date 2016年11月16日 下午2:00:25
 */
public class HandshakeResponseBuilder extends MessageBuilder {
	
	@Override
	public HandshakeResponseMessage build() {
		return new HandshakeResponseMessage(this);
	}
	
}
