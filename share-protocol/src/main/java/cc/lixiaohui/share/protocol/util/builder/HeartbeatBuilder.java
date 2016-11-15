package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.protocol.HeartbeatMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:13:44
 */
public class HeartbeatBuilder extends MessageBuilder {

	@Override
	public HeartbeatMessage build() {
		return new HeartbeatMessage(this);
	}
}
