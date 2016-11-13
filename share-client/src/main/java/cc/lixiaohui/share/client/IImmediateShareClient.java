package cc.lixiaohui.share.client;

import java.util.Collection;

/**
 * 即时通信客户端接口定义
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午9:54:54
 */
public interface IImmediateShareClient{
	
	/**
	 * 发送聊天消息给好友
	 * @param userId 用户ID 
	 * @param message 消息
	 * @throws ShareClientException
	 */
	void send(int userId, String message) throws ShareClientException;
	
	/**
	 * 发送聊天消息给好友
	 * @param userId 用户ID
	 * @param message 消息
	 * @param listener 消息监听器
	 * @throws ShareClientException
	 */
	void send(int userId, String message, ISentMessageListener listener) throws ShareClientException;
	
	/**
	 * 添加消息监听器
	 * @param listener 消息监听器
	 */
	void addMessageListener(IMessageListener listener);
	
	/**
	 * 添加多个消息监听器
	 * @param listeners
	 */
	void addMessageListeners(Collection<IMessageListener> listeners);
	
	/**
	 * 移除消息监听器
	 * @param listener
	 */
	void removeMessageListener(IMessageListener listener);
	
	
	/**
	 * 已发送的消息的监听器, 比如当你想知道你发送的消息是否送达服务器、是否送达目标客户端, 
	 * 你就应该在对应的回调方法中实现对应的逻辑
	 * 
	 * @author lixiaohui
	 * @date 2016年11月6日 上午1:22:15
	 */
	public interface ISentMessageListener {
		
		/**
		 * 服务端已接收到消息了
		 */
		void onServerReceieved();
		
		/**
		 * 目标客户端接收到消息了
		 */
		void onTargetClientRecieved();
		
		/**
		 * 消息发送失败了
		 * @param reason 失败原因描述
		 */
		void onSendFailed(String reason);
	}
	
}
