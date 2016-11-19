package cc.lixiaohui.share.client;

import java.util.Collection;

import cc.lixiaohui.share.client.util.ClientException;

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
	 * @throws ClientException
	 */
	void send(int userId, String message) throws ClientException;
	
	/**
	 * 发送聊天消息给好友
	 * @param userId 用户ID
	 * @param message 消息
	 * @param listener 消息监听器
	 * @throws ClientException
	 */
	void send(int userId, String message, ISentMessageListener listener) throws ClientException;
	
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
	 * 已发送的消息监听器
	 * 
	 * @author lixiaohui
	 * @date 2016年11月6日 上午1:22:15
	 */
	public interface ISentMessageListener {
		
		/**
		 * 发出去的消息收到响应了
		 * 1.发送成功时:
		 * {
		 *   "status":0,
		 *   "timestamp":1434232243,
		 *   "msg":"成功发送",
		 *   "result":{}
		 * }
		 * 2.发送失败时:
		 * {
		 *   "status":1,
		 *   "timestamp":1443242432,
		 *   "errmsg":"发送消息出错啦"		# 出错信息
		 *   "errcode":1,				# 错误码
		 *   "expmsg":"异常信息"			# 异常信息, 该域不一定有值
		 *   "result":
		 *     {
		 *       "origin": {"toUserId":2, "content":"你好"}	# 原始消息
		 *     }
		 * }
		 * @param reason 失败原因描述
		 */
		void onResponsed(String respJson);
	}
	
}
