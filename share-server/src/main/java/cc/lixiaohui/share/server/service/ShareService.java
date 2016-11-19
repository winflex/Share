package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.model.bean.Comment;
import cc.lixiaohui.share.model.bean.Picture;
import cc.lixiaohui.share.model.bean.Praise;
import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.model.dao.util.DaoException;
import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.Objects;
import cc.lixiaohui.share.util.SQLUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:09:41
 */
@Service(name = "ShareService")
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
	 * @param deleted boolean, nullable default false
	 * @return
	 * 
	 * @throws ServiceException
	 */
	@Procedure(name = "getShares")
	public String getShares() throws ServiceException {
		try {
			String keyword = SQLUtils.toLike(getStringParameter("keyword", ""));
			int orderColumn = getIntParameter("orderColumn", DEFAULT_ORDER_COLUMN_INDEX);
			int orderType = getIntParameter("orderType", DEFAULT_ORDER_TYPE_INDEX);
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			boolean deleted = getBooleanParameter("deleted", false);
			
			ShareDao dao = daofactory.getDao(ShareDao.class);
			List<Share> shares = dao.list(keyword, deleted, getOrderFieldName(orderColumn), getOrderType(orderType), start, limit);
			return JSONUtils.newSuccessfulResult("获取分享成功", packMultiShare(shares));
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}
	
	private JSONObject packMultiShare(List<Share> shares) {
		JSONArray shareArray = new JSONArray();
		for (Share share : shares) {
			JSONObject item = new JSONObject();
			item.put("id", share.getId());
			item.put("userId", share.getPublisher().getId());
			item.put("username", share.getPublisher().getUsername());
			item.put("content", share.getContent());
			item.put("createTime", share.getCreateTime());
			item.put("praiseCount", share.getPraiseCount());
			item.put("commentCount", share.getCommentCount());
			JSONArray pictureArray = new JSONArray();
			for (Picture p : share.getPictures()) {
				pictureArray.add(p.getId());
			}
			item.put("pictureIds", pictureArray);
			shareArray.add(item);
		}
		JSONObject result = new JSONObject();
		result.put("count", shares.size());
		result.put("shares", shareArray);
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
	 * @param shareId int, !nullable
	 * @return
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
	 * @throws ServiceException
	 */
	@Procedure(name = "getShare")
	public String getShare() throws ServiceException {
		try {
			int shareId = getIntParameter("shareId");
			
			ShareDao shareDao = daofactory.getDao(ShareDao.class);
			Share share = shareDao.getByIdForDetail(shareId);
			
			if (share == null) {
				return JSONUtils.newFailureResult("分享不存在", ErrorCode.RESOURCE_NOT_FOUND, "");
			}
			List<Picture> pictures = share.getPictures();
			List<Praise> praises = share.getPraises();
			List<Comment> comments = share.getComments();
			return JSONUtils.newSuccessfulResult("获取成功", packSingleShare(share, pictures, praises, comments));
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), "");
		}
	}
	
	private JSONObject packSingleShare(Share share, List<Picture> pictures, List<Praise> praises, List<Comment> comments) {
		// praises
		JSONArray praiseArray = new JSONArray();
		for (Praise p : praises) {
			JSONObject item = new JSONObject();
			item.put("userId", p.getUser().getId());
			item.put("useranme", p.getUser().getUsername());
			praiseArray.add(item);
		}
		JSONObject praiseInfo = new JSONObject();
		praiseInfo.put("praiseCount", praises.size());
		praiseInfo.put("praiseUsers", praiseArray);
		
		// comments
		JSONArray commentArray = new JSONArray();
		for (Comment c : comments) {
			JSONObject item = new JSONObject();
			item.put("commentId", c.getId());
			item.put("content", c.getContent());
			item.put("commentTime", c.getCommentTime());
			item.put("fromUserId", c.getFromUser().getId());
			item.put("fromUsername", c.getFromUser().getUsername());
			item.put("toUserId", c.getToUser().getId());
			item.put("toUsername", c.getToUser().getUsername());
			commentArray.add(item);
		}
		JSONObject commentInfo = new JSONObject();
		commentInfo.put("commentCount", comments.size());
		commentInfo.put("commentList", commentArray);
		
		JSONObject result = new JSONObject();
		result.put("id", share.getId());
		result.put("userId", share.getPublisher().getId());
		result.put("username", share.getPublisher().getUsername());
		result.put("content", share.getContent());
		result.put("createTime", share.getCreateTime());
		result.put("praiseInfo", praiseInfo);
		result.put("commentInfo", commentInfo);
		return result;
	}

	/**
	 * @param shareId int !nullable
	 * @param physically boolean nullable default false 是否物理删除
	 * <pre>
	 * int shareId
	 * {}
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "deleteShare", level=PrivilegeLevel.LOGGED)
	public String deleteShare() throws ServiceException {
		try {
			int shareId = getIntParameter("shareId");
			boolean physically = getBooleanParameter("physically", false);
			ShareDao dao = daofactory.getDao(ShareDao.class);
			int rows;
			if (physically) {
				rows = dao.delete(shareId);
			} else {
				rows = dao.vitualDelete(shareId);
			}
			if (rows > 0) {
				JSONObject result = new JSONObject();
				result.put("shareId", shareId);
				return JSONUtils.newSuccessfulResult("删除成功", result);
			} else {
				return JSONUtils.newFailureResult("删除失败", ErrorCode.UNKOWN, "");	
			}
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
	}
	
	/**
	 * 
	 * @param content String, nullable
	 * @param pictureIds int[], nullable
	 * @return
	 * <pre>
	 * String content, int[] picturesIds
	 * {
	 *     "shareId":321                  # 发布的分享的ID
	 * }
	 * </pre>
	 * @throws ServiceException
	 */
	@Procedure(name = "publishShare", level=PrivilegeLevel.LOGGED)
	public String publishShare() throws ServiceException {
		if (!session.isLogined()) {
			return JSONUtils.newFailureResult("您未登陆", ErrorCode.AUTH, "");
		}
		
		try {
			String content = getStringParameter("content", "");
			int[] pictureIds = getObjectParameter("pictureIds", Objects.EMPTY_INT_ARRAY);
			
			if (content.equals("") && pictureIds.length == 0) {
				return JSONUtils.newFailureResult("不能发布空的分享", ErrorCode.UNKOWN, "");
			}
			
			UserDao userDao = daofactory.getDao(UserDao.class);
			ShareDao shareDao = daofactory.getDao(ShareDao.class);
			PictureDao pictureDao = daofactory.getDao(PictureDao.class);
			User user = userDao.getById(session.getUserId());
			Share share = newShare(content, pictureIds, pictureDao.getPictures(pictureIds), user);
			
			if (shareDao.add(share) > 0) {
				JSONObject result = new JSONObject();
				result.put("shareId", share.getId());
				// TODO 推送, 注意:不要推送给源客户端
				if (session.isSelfShield() || session.isAdminShield()) {
					logger.warn("ignoring shield session {}", session);
				} else {
					PushMessage message = PushMessage.builder().type(PushMessage.Type.SHARE)
							.pushData(packSharePushResult(share).toJSONString()).build();
					//推送是异步的
					session.getSessionManager().publishPushMessage(session, message);
				}
				return JSONUtils.newSuccessfulResult("发布分享成功", result);
			} else {
				return JSONUtils.newFailureResult("发布分享失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
	}

	private JSONObject packSharePushResult(Share share) {
		JSONArray pictureArray = new JSONArray();
		for (Picture p : share.getPictures()) {
			pictureArray.add(p.getId());
		}
		
		JSONObject pushResult = new JSONObject();
		pushResult.put("shareId", share.getId());
		pushResult.put("userId", share.getPublisher().getId());
		pushResult.put("username", share.getPublisher().getUsername());
		pushResult.put("content", share.getContent());
		pushResult.put("username", share.getCreateTime());
		pushResult.put("pictureIds", pictureArray);
		return pushResult;
	}

	private Share newShare(String content, int[] pictureIds, List<Picture> pictures, User user) throws DaoException {
		Share share = new Share();
		share.setPublisher(user);
		share.setContent(content);
		share.setPictures(pictures);
		return share;
	}
}
