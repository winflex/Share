package cc.lixiaohui.share.protocol;

import java.util.HashMap;
import java.util.Map;

import cc.lixiaohui.share.protocol.util.builder.ConnectionMessageBuilder;

/**
 * 连接建立时, 服务端主动给客户端发的消息, 包含连接的相关参数
 * 
 * @author lixiaohui
 * @date 2016年11月8日 下午10:34:03
 */
public class HandShakeMessage extends Message {

	private static final long serialVersionUID = 6144729504636857321L;

	/**
	 * 服务映射: User -> cc.lixiaohui.share.server.service.UserService
	 */
	private Map<String, String> serviceMap = new HashMap<String, String>();
	
	private long heartbeatInterval;

	public HandShakeMessage(ConnectionMessageBuilder builder) {
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

	public Map<String, String> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, String> serviceMap) {
		this.serviceMap = serviceMap;
	}
}
