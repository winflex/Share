package cc.lixiaohui.share.client;

/**
 * 消息监听器
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午4:14:58
 */
public interface IMessageListener {
	
	/**
	 * 接收到别的用户发来的消息
	 * @param fromUserId 该消息的发送者的ID
	 * @param message 文本消息
	 */
	void onMessage(int fromUserId, String message);
	
}
