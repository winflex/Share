package cc.lixiaohui.share.server.service;

import java.util.Map;

import cc.lixiaohui.share.server.Session;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:09:41
 */
public class ShareService extends AbstractService {

	public ShareService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * String keyword, int order, int start, int limit
	 * <pre>
	  { 
	 *   "count":2,    # 所返回的结果条数
	 *   "shares": [      # 结果数组
	 *   	{
	 *   	 "hasRead":0,
	 *   	 "share":
	 *   		{
	 *   	 		"id":11,                  # 分享ID
	 *   	 		"userId":222,             # 所属用户ID
	 *       		"username":lixiaohui,     # 所属用户名
	 *       		"content":"nice day!!",   # 分享文字内容
	 *       		"createTime":14743432423, # 分享创建时间
	 *       		"praiseCount":23,         # 点赞数
	 *       		"commentCount":32,        # 评论数
	 *       		"pictures":[12,32,43,43]  # 分享包含的图片, 请另外使用getPicture(int pictureId)获取图片字节流
	 *          }
	 *      },
	 *      {
	 *       "hasRead":0,
	 *       "share":
	 *       	{
	 *       		"id":12,                  # 分享ID
	 *     	 		"userId":333,             # 所属用户ID
	 *       		"username":lixiaohui,     # 所属用户名
	 *       		"content":"bad day!!",    # 分享文字内容
	 *       		"createTime":14743432423, # 分享创建时间
	 *       		"praiseCount":23,         # 点赞数
	 *       		"commentCount":32,        # 评论数
	 *       		"pictures":[12,32,43,43]  # 分享包含的图片, 请另外使用getPicture(int pictureId)获取图片字节流
	 *      	}
	 *      }
	 *   ]
	 * }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	public String getShares() throws ServiceException {
		return null;
	}
	
	/**
	 * int shareId
	 * <pre>
	 *  {
	 *       "id":321,                 # 分享的ID
	 *       "userId":12,              # 所属用户ID
	 *       "username":lixiaohui,     # 所属用户名
	 *       "content":"bad day!!",    # 分享文字内容
	 *       "createTime":14743432423, # 分享创建时间
	 *       "praiseInfo":             # 点赞信息
	 *         {
	 *           "praiseCount":2,      # 赞的个数
	 *           "praiseUsers":        # 所有点赞的用户
	 *             [
	 *               {"userId":1, "username":"李小辉"},
	 *               {"userId":2, "username":"李大辉"}
	 *             ]
	 *         }
	 *       "comments":               # 评论信息
	 *         {
	 *           "commentCount":1,     # 评论总数
	 *           "commentList":
	 *             [
	 *               {
	 *                 "commentId":1,             # 评论ID
	 *                 "shareId":2,               # 所属分享的ID
	 *                 "content":"哈哈啊哈哈",       # 评论的内容
	 *                 "kind":1,                  # 评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论
	 *                 "commentTime":1467326722,  # 评论时间
	 *                 "fromUserId":1             # 评论者ID
	 *                 "fromUsername":"lixiaohui" # 评论者用户名
	 *                 "toUserId":2               # 被评论的用户ID
	 *                 "toUsername":"zhangsan"    # 被评论的用户ID
	 *               }
	 *             ]
	 *         }
	 *       "pictures":[12,32,43,43]  # 分享包含的图片, 请另外使用getPicture(int pictureId)获取图片字节流
	 *     }
	 * }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	public String getShare() throws ServiceException {
		return null;
	}
	
	/**
	 * <pre>
	 * int shareId
	 * {}
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	public String deleteShare() throws ServiceException {
		return null;
	}
	
	/**
	 * <pre>
	 * String content, int[] picturesIds
	 * {
	 *     "id":321                  # 发布的分享的ID
	 * }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	public String publishShare() throws ServiceException {
		return null;
	}
}
