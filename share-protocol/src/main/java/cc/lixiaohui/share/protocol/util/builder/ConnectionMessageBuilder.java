package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.protocol.HandShakeMessage;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:44:22
 */
public class ConnectionMessageBuilder extends MessageBuilder {
	
	protected long heartbeatInterval;
	
	public long heartbeatInterval() {
		return heartbeatInterval;
	}

	public ConnectionMessageBuilder heartbeatInterval(long heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
		return this;
	}
	
	@Override
	public HandShakeMessage build() {
		return new HandShakeMessage(this);
	}
}
