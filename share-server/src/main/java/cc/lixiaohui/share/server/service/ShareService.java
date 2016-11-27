package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.model.bean.Comment;
import cc.lixiaohui.share.server.model.bean.Picture;
import cc.lixiaohui.share.server.model.bean.Praise;
import cc.lixiaohui.share.server.model.bean.Share;
import cc.lixiaohui.share.server.model.bean.ShareReadRecord;
import cc.lixiaohui.share.server.model.bean.User;
import cc.lixiaohui.share.server.model.dao.PictureDao;
import cc.lixiaohui.share.server.model.dao.ShareDao;
import cc.lixiaohui.share.server.model.dao.UserDao;
import cc.lixiaohui.share.server.model.dao.util.DaoException;
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
		
		ORDER_TYPE_MAP.put(0, "desc"); // 逆序, 大到小
		ORDER_TYPE_MAP.put(1, "asc");
	}
	
	private static final int DEFAULT_ORDER_TYPE_INDEX = 0;
	private static final int DEFAULT_ORDER_COLUMN_INDEX = 0;
	
	public ShareService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * 获取多个分享
	 * 1.若当前session未登陆, 则要考虑屏蔽情况
	 * 2.否则, 自己的selfForbid和adminForbid忽略, 但是他人的selfForbid和adminForbid要考虑
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
	 * @param baseTime long, nullable
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
			int targetUserId = getIntParameter("userId", -1);
			long baseTime = getLongParameter("baseTime", 0);
			int orderColumn = getIntParameter("orderColumn", DEFAULT_ORDER_COLUMN_INDEX);
			int orderType = getIntParameter("orderType", DEFAULT_ORDER_TYPE_INDEX);
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			boolean deleted = getBooleanParameter("deleted", false);
			
			ShareDao dao = daofactory.getDao(ShareDao.class);
			int userId = session.isLogined() ? session.getUser().getId() : -1;
			List<Share> shares = dao.list(userId, targetUserId, keyword, baseTime, deleted, getOrderColumn(orderColumn), getOrderType(orderType), start, limit);
			return JSONUtils.newSuccessfulResult("获取分享成功", packMultiShare(shares, session));
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}
	
	/**
	 * 获取单个分享的详细信息
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
	
	/**
	 * 删除分享
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
	 * 发布分享
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
			User user = userDao.getById(session.getUser().getId());
			Share share = newShare(content, pictureIds, pictureDao.getPictures(pictureIds), user);
			
			if (shareDao.add(share) > 0) {
				JSONObject result = new JSONObject();
				result.put("shareId", share.getId());
				// TODO 推送给除了发布者外的所有在线客户端
				if (session.isSelfShield() || session.isAdminShield()) {
					logger.warn("ignoring shield session {}", session);
				} else {
					try {
						PushMessage message = PushMessage.builder().type(PushMessage.Type.SHARE)
								.pushData(packSharePushResult(share).toJSONString()).build();
						//推送是异步的
						session.getSessionManager().pushToAllExcept(session, message);
					} catch (Throwable t) {
						logger.error("{}", t);
					}
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
	
	/**
	 * 获取评论过的分享
	 * @param start int, nullable 
	 * @param limit int, nullable
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name="getCommentedShares", level=PrivilegeLevel.LOGGED)
	public String getCommentedShares() throws ServiceException {
		try {
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			ShareDao shareDao = daofactory.getDao(ShareDao.class);
			List<Object[]> commentAndShares = shareDao.listCommentedByUser(session.getUser().getId(), start, limit);
			return JSONUtils.newSuccessfulResult("获取分享成功", packCommentedShares(commentAndShares));
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause);
		}
	}

	/**
	 * 获取点赞过的分享
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name="getLikedShares", level=PrivilegeLevel.LOGGED)
	public String getLikedShares() throws ServiceException {
		try {
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			ShareDao shareDao = daofactory.getDao(ShareDao.class);
			List<Object[]> shareAndPraises = shareDao.listLikedByUser(session.getUser().getId(), start, limit);
			return JSONUtils.newSuccessfulResult("获取分享成功", packLikedShares(shareAndPraises));
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause);
		}
	}
	
	private JSONObject packLikedShares(List<Object[]> shareAndPraises) {
		JSONArray items = new JSONArray();
		for (Object[] arr : shareAndPraises) {
			Share share = (Share) arr[0];
			Praise praise = (Praise) arr[1];
			
			JSONObject shareObject = new JSONObject();
			shareObject.put("id", share.getId());
			shareObject.put("userId", share.getPublisher().getId());
			shareObject.put("username", share.getPublisher().getUsername());
			shareObject.put("content", share.getContent());
			shareObject.put("createTime", share.getCreateTime());
			shareObject.put("praiseCount", share.getPraiseCount());
			shareObject.put("commentCount", share.getCommentCount());
			
			JSONObject praiseObject = new JSONObject();
			praiseObject.put("id", praise.getId());
			praiseObject.put("praiseTime", praise.getPraiseTime());
			praiseObject.put("userId", praise.getUser().getId());
			praiseObject.put("share", shareObject);
			
			items.add(praiseObject);
		}
		JSONObject result = new JSONObject();
		result.put("count", shareAndPraises.size());
		result.put("list", items);
		return result;
	}

	private JSONObject packCommentedShares(List<Object[]> commentAndShares) {
		JSONArray items = new JSONArray();
		for (Object[] arr : commentAndShares) {
			Share share = (Share) arr[0];
			Comment comment = (Comment) arr[1];
			
			JSONObject shareObject = new JSONObject();
			shareObject.put("id", share.getId());
			shareObject.put("userId", share.getPublisher().getId());
			shareObject.put("username", share.getPublisher().getUsername());
			shareObject.put("content", share.getContent());
			shareObject.put("createTime", share.getCreateTime());
			shareObject.put("praiseCount", share.getPraiseCount());
			shareObject.put("commentCount", share.getCommentCount());
			
			JSONObject commentObject = new JSONObject();
			commentObject.put("id", comment.getId());
			commentObject.put("content", comment.getContent());
			commentObject.put("commentTime", comment.getCommentTime());
			commentObject.put("share", shareObject);
			commentObject.put("fromUserId", comment.getFromUser().getId());
			commentObject.put("fromUsername", comment.getFromUser().getUsername());
			commentObject.put("toUserId", comment.getToUser().getId());
			commentObject.put("toUsername", comment.getToUser().getUsername());
			
			items.add(commentObject);
		}
		JSONObject result = new JSONObject();
		result.put("count", commentAndShares.size());
		result.put("list", items);
		return result;
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

	private JSONObject packMultiShare(List<Share> shares, Session session) {
		JSONArray shareArray = new JSONArray();
		for (Share share : shares) {
			JSONObject shareItem = new JSONObject();
			shareItem.put("id", share.getId());
			shareItem.put("userId", share.getPublisher().getId());
			shareItem.put("username", share.getPublisher().getUsername());
			shareItem.put("content", share.getContent());
			shareItem.put("createTime", share.getCreateTime());
			shareItem.put("praiseCount", share.getPraiseCount());
			shareItem.put("commentCount", share.getCommentCount());
			JSONArray pictureArray = new JSONArray();
			for (Picture p : share.getPictures()) {
				pictureArray.add(p.getId());
			}
			shareItem.put("pictureIds", pictureArray);
			
			JSONObject item = new JSONObject();
			boolean hasRead = false;
			if (session.isLogined()) {
				int userId = session.getUser().getId();
				for (ShareReadRecord record : share.getReadRecords()) {
					if (record.getUser().getId() == userId) {
						hasRead = true;
						break;
					}
				}
			}
			item.put("hasRead", hasRead);
			item.put("readCount", share.getReadRecords().size());
			item.put("share", shareItem);
			shareArray.add(item);
		}
		JSONObject result = new JSONObject();
		result.put("count", shares.size());
		result.put("shares", shareArray);
		return result;
	}

	private String getOrderColumn(int order) {
		String name = ORDER_FIELD_MAP.get(order);
		return name == null ? ORDER_FIELD_MAP.get(0) : name;
	}

	private String getOrderType(int orderType) {
		String name = ORDER_TYPE_MAP.get(orderType);
		return name == null ? ORDER_FIELD_MAP.get(0) : name;
	}
	
	private Share newShare(String content, int[] pictureIds, List<Picture> pictures, User user) throws DaoException {
		Share share = new Share();
		share.setPublisher(user);
		share.setContent(content);
		share.setPictures(pictures);
		return share;
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

}
