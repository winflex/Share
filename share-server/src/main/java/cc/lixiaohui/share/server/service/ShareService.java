package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:09:41
 */
public class ShareService extends AbstractService {
	
	public static final Map<Integer, String> ORDER_FIELD_MAP = new HashMap<Integer, String>();
	
	public static final Map<Integer, String> ORDER_TYPE_MAP = new HashMap<Integer, String>();
	
	static {
		ORDER_FIELD_MAP.put(0, "createTime");
		ORDER_FIELD_MAP.put(1, "praiseCount");
		ORDER_FIELD_MAP.put(2, "commentCount");
		
		ORDER_TYPE_MAP.put(0, "desc");
		ORDER_TYPE_MAP.put(1, "asc");
	}
	
	private static final int DEFAULT_ORDER_TYPE_INDEX = 0;
	private static final int DEFAULT_ORDER_COLUMN_INDEX = 0;
	
	private static final String DEFAULT_LIKE_EXPR = "%%";

	public ShareService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * 
	 * <pre>
	  { 
	 *   "count":1,    # 所返回的结果条数
	 *   "shares": [      # 结果数组
	 *   	{
	 *   	 "hasRead":true,
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
	 *      }
	 *   ]
	 * }
	 * </pre>
	 * String keyword, int orderColumn, int orderType, int start, int limit
	 * @param keyword String, nullable
	 * @param orderColumn int, nullable,
	 * @param start int, nullable default 0
	 * @param limit int, nullable default 20
	 * @return
	 * 
	 * @throws ServiceException
	 */
	public String getShares() throws ServiceException {
		String keyword = null;
		int orderColumn = -1;
		int orderType = -1;
		int start = -1;
		int limit = -1;
		try {
			keyword = getStringParameter("keyword", null);
			keyword = keyword == null ? DEFAULT_LIKE_EXPR : "%" + keyword + "%"; // %keyword%
			orderColumn = getIntParameter("orderColumn", DEFAULT_ORDER_COLUMN_INDEX);
			orderType = getIntParameter("orderType", DEFAULT_ORDER_TYPE_INDEX);
			start = getIntParameter("start", 0);
			limit = getIntParameter("limit", 20);
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
		String orderColumnName = getOrderFieldName(orderColumn);// order column
		String orderTypeName = getOrderType(orderType); // desc or asc
		try {
			ShareDao dao = daofactory.getDao(ShareDao.class);
			String hql =  String.format("select  from Share s "
					+ "where s.content like '%s' "
					+ "order by s.%s %s limit %d,%d"
					, keyword, orderColumnName, orderTypeName, start, limit);
			List<Share> shares = dao.list(hql);
			return JSONUtils.newSuccessfulResult("获取分享成功", packMultiShare(shares));
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}
	
	private JSONObject packMultiShare(List<Share> shares) {
		JSONObject result = new JSONObject();
		return result;
	}

	private String getOrderFieldName(int order) {
		String name = ORDER_FIELD_MAP.get(order);
		return name == null ? ORDER_FIELD_MAP.get(0) : name;
	}

	private String getOrderType(int orderType) {
		String name = ORDER_TYPE_MAP.get(orderType);
		return name == null ? ORDER_FIELD_MAP.get(0) : name;
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
