package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.HeartbeatBuilder;

/**
 * 心跳, 无任何负载
 * @author lixiaohui
 * @date 2016年11月7日 下午10:08:50
 */
public class HeartbeatMessage extends Message {

	private static final long serialVersionUID = -6719463444605674904L;
	
	public HeartbeatMessage() {}
	
	public HeartbeatMessage(HeartbeatBuilder builder) {
		super(builder);
	}
	
	public static HeartbeatBuilder builder() {
		return new HeartbeatBuilder();
	}
}
