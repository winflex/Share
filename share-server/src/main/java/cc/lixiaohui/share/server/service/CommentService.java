package cc.lixiaohui.share.server.service;

import java.util.Map;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:00:25
 */
@Service(name = "CommentService")
public class CommentService extends AbstractService {

	public CommentService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * int shareId, int start, int limit
	 * <pre>
	 * {
	 *   "count":2
	 *   "list":[
	 *    {
	 *     "id":11,                   # 评论ID
	 *     "shareId":32               # 所属分享的ID
	 *     "content":"good!!",        # 评论的内容
	 *     "kind":0                   # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *     "commentTime":1465565576,  # 评论时间
	 *     "fromUserId":1             # 评论者ID
	 *     "fromUsername":"lixiaohui" # 评论者用户名
	 *     "toUserId":2               # 被评论的用户ID
	 *     "toUsername":"zhangsan"    # 被评论的用户ID
	 *    },
	 *    {
	 *     "id":11,                   # 评论ID
	 *     "shareId":32               # 所属分享的ID
	 *     "content":"good!!",        # 评论的内容
	 *     "kind":0                   # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *     "commentTime":1465565576,  # 评论时间
	 *     "fromUserId":1             # 评论者ID
	 *     "fromUsername":"lixiaohui" # 评论者用户名
	 *     "toUserId":2               # 被评论的用户ID
	 *     "toUsername":"zhangsan"    # 被评论的用户ID
	 *    }
	 *   ]
	 * }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "getCommentOfShare")
	public String getCommentOfShare() throws ServiceException {
		return null;
	}
	
	/**
	 * <pre>
	 * int start, int limit
	 * {
	 *       "count":2
	 *       "list":
	 *         [
	 *           {
	 *             "id":11,                   # 评论ID
	 *             "shareId":32               # 所属分享的ID
	 *             "content":"good!!",        # 评论的内容
	 *             "kind":0                   # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *             "commentTime":1465565576,  # 评论时间
	 *             "fromUserId":1             # 评论者ID
	 *             "fromUsername":"lixiaohui" # 评论者用户名
	 *             "toUserId":2               # 被评论的用户ID
	 *             "toUsername":"zhangsan"    # 被评论的用户ID
	 *           },
	 *           {
	 *             "id":11,                   # 评论ID
	 *             "shareId":32               # 所属分享的ID
	 *             "content":"good!!",        # 评论的内容
	 *             "kind":0                   # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *             "commentTime":1465565576,  # 评论时间
	 *             "fromUserId":1             # 评论者ID
	 *             "fromUsername":"lixiaohui" # 评论者用户名
	 *             "toUserId":2               # 被评论的用户ID
	 *             "toUsername":"zhangsan"    # 被评论的用户ID
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "getCommentOfUser")
	public String getCommentOfUser() throws ServiceException {
		return null;
	}
	
	/**
	 * int commentId
	 * {}
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "deleteCommentOfUser")
	public String deleteCommentOfUser() throws ServiceException {
		return null;
	}
	
	/**
	 * int shareId, String content, int toUserId
	 * {}
	 * @return
	 */
	@Procedure(name = "commentUser")
	public String commentUser() throws ServiceException{
		return null;
	}
	
	/**
	 * int shareId, String content
	 * {}
	 * @return 
	 */
	@Procedure(name = "commentShare")
	public String commentShare() throws ServiceException{
		return null;
	}
}
