package cc.lixiaohui.share.client;

/**
 * 推送消息监听器
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午4:14:58
 */
public interface IMessageListener {
	
	/**
	 * 收到聊天信息
	 * 
	 * {
	 *   "fromUserId":1,
	 *   "content":"你好",
	 *   "sendTime":1343243243
	 * }
	 * @param userId 对方的用户ID
	 * @param message 文本消息
	 * @param sendTime 发送时间
	 */
	void onChat(int fromUserId, String content, long sendTime);
	
	
	/**
	 * 新的分享推送
	 * @param shareJson
	 * <pre>
	 * json格式:
	 * {
	 *   "shareId":1,					# 分享ID
	 *   "userId":2,					# 发布该分享用户ID
	 *   "username":"sfsfs",			# 发布该分享的用户名
	 *   "content":"今天",				# 分享的文字内容
	 *   "createTime":143353454354,		# 分享创建的时间
	 *   "pictures":[1,2,3,4,5]			# 分享包含的图片
	 * }
	 * </pre>
	 */
	void onShare(String shareJson);
	
	/**
	 * 收到新的好友请求
	 * @param json
	 *  <pre>
	 * json格式:
	 * {
	 *   "userId":1,			# 对方的ID
	 *   "username":"猪八叽"		# 对方的用户名
	 * }
	 * </pre>
	 */
	void onFriendRequest(String json);
	
	/**
	 * 收到之前发的好友请求的答复
	 * @param json
	 * json格式:
	 * <pre>
	 * 	{
	 * 		"targetUserId":1,			# 对方用户的ID
	 * 		"targetUsername":"猪八戒"		# 对方用户名
	 * 		"accept":false,				# true说明朋友关系确立, false 说明对方拒绝添加, 此时可以通过拿msg字段获取拒绝理由
	 * 		"msg":"丑拒"					# 说明			
	 * 	}
	 * </pre>
	 */
	void onFriendResponse(String json);
	
	/**
	 * 收到好友删除通知
	 * @param json
	 * <pre>
	 * json格式:
	 * {
	 *   "userId":1,			# 对方的ID
	 *   "username":"猪八戒"		# 对方的用户名
	 * }
	 * </pre>
	 */
	void onFriendDeleted(String json);
	
	/**
	 * 收到新的评论
	 * @param json
	 * <pre>
	 * {
	 *     "id":11,                   # 评论ID
	 *     "shareId":32               # 所属分享的ID
	 *     "content":"good!!",        # 评论的内容
	 *     "commentTime":1465565576,  # 评论时间
	 *     "fromUserId":1             # 评论者ID
	 *     "fromUsername":"lixiaohui" # 评论者用户名
	 *     "toUserId":2               # 被评论的用户ID
	 *     "toUsername":"zhangsan"    # 被评论的用户名
	 * }
	 * </pre>
	 */
	void onComment(String json);
	
	/**
	 * 收到新的点赞
	 * @param json
	 * <pre>
	 * {
	 *   "id":1,						# 赞的ID
	 *   "shareId":1,					# 点赞的分享ID
	 *   "praiseUserId":2,				# 点赞的用户ID	
	 *   "praiseUsername":"猪八戒"		# 点赞的用户的用户名
	 *   "time":14432432432				#  点赞时间
	 * }
	 * </pre>
	 */
	void onLike(String json);
	
	/**
	 * 收到取消点赞的通知
	 * @param json
	 * <pre>
	 * {
	 * 		"id":1,						# 赞的ID
	 * 		"shareId":1,				# 分享ID
	 * 		"praiseUserId":2,			# 用户ID
	 * 		"praiseUsername":"猪八戒"		# 用户名
	 * 		"time":1443242342,			# 取消点赞时间
	 * }
	 * </pre>
	 */
	void onUnlike(String json);
}
