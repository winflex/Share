package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.ConnectionMessageBuilder;

/**
 * 连接建立时, 服务端主动给客户端发的消息, 包含连接的相关参数
 * 
 * @author lixiaohui
 * @date 2016年11月8日 下午10:34:03
 */
public class ConnectionMessage extends Message {

	private static final long serialVersionUID = 6144729504636857321L;

	private long heartbeatInterval;

	public ConnectionMessage(ConnectionMessageBuilder builder) {
		super(builder);
		this.heartbeatInterval = builder.heartbeatInterval();
	}

	/**
	 * @return the heartbeatInterval
	 */
	public long getHeartbeatInterval() {
		return heartbeatInterval;
	}

	/**
	 * @param heartbeatInterval
	 *            the heartbeatInterval to set
	 */
	public void setHeartbeatInterval(long heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static ConnectionMessageBuilder builder() {
		return new ConnectionMessageBuilder();
	}
}
