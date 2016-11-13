package cc.lixiaohui.share.protocol.util.builder;

import cc.lixiaohui.share.protocol.ConnectionMessage;

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
	public ConnectionMessage build() {
		return new ConnectionMessage(this);
	}
}
