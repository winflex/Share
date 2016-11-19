package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.HandshakeResponseBuilder;

/**
 * @author lixiaohui
 * @date 2016年11月15日 上午11:21:08
 */
public class HandshakeResponseMessage extends Message {

	private static final long serialVersionUID = -455405514247956309L;

	public HandshakeResponseMessage() {}
	
	public HandshakeResponseMessage(HandshakeResponseBuilder builder) {
		super(builder);
	}

	public static HandshakeResponseBuilder builder() {
		return new HandshakeResponseBuilder();
	}
}
