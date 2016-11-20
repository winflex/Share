package cc.lixiaohui.share.server.service;

import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.model.bean.ShareCollection;
import cc.lixiaohui.share.model.bean.UserCollection;
import cc.lixiaohui.share.model.dao.ShareCollectionDao;
import cc.lixiaohui.share.model.dao.UserCollectionDao;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:24:56
 */
@Service(name = "CollectionService")
public class CollectionService extends AbstractService {

	public CollectionService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * int start, int limit
	 * @param start int, nullable default 0
	 * @param limit int, nullable default 20
	 * <pre>
	 * {
	 *       "count":1,                          # 获取的用户总数
	 *       "users":                      		 # 用户列表
	 *         [
	 *           {
	 *             "collectionId":12,            # 该收藏ID
	 *             "collectTime":14342432423,    # 收藏时间
	 *             "user":                       # 对应的用户
	 *               {
	 *                 "userId":32,              # 用户ID
	 *                 "username":"李晓明",        # 用户名
	 *                 "headImageId":321         # 头像ID
	 *               }
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "getUserCollection", level=PrivilegeLevel.LOGGED)
	public String getUserCollection() throws ServiceException {
		try {
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			UserCollectionDao dao = daofactory.getDao(UserCollectionDao.class);
			List<UserCollection> collections = dao.getByUserId(session.getUser().getId(), start, limit);
			return JSONUtils.newSuccessfulResult("获取收藏用户成功", packUserCollection(collections));
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), "");
		}
	}
	
	/**
	 * <pre>
	 * int start, int limit
	 * {
	 *       "count":1,                                   # 查询到的收藏总数
	 *       "collections":                               # 查询到的收藏列表
	 *         [
	 *           {
	 *             "collectionId":11,
	 *             "collectTime":14432423432,             # 收藏的时间
	 *             "share":                               # 对应的分享
	 *               {
	 *                 "shareId":1,                       # 分享ID
	 *                 "content":321,                     # 该分享的内容
	 *                 "createTime":14342424,             # 该分享创建的时间
	 *                 "praiseCount":432,                 # 赞数
	 *                 "commentCount":312                 # 评论数
	 *               }
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "getShareCollection", level=PrivilegeLevel.LOGGED)
	public String getShareCollection() throws ServiceException {
		try {
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			ShareCollectionDao dao = daofactory.getDao(ShareCollectionDao.class);
			List<ShareCollection> collections = dao.getByUserId(session.getUser().getId(), start, limit);
			return JSONUtils.newSuccessfulResult("获取收藏分享成功", packShareCollection(collections));
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), "");
		}
	}

	/**
	 * 取消对用户的收藏
	 * @param collectionId int, !nullable
	 * @return
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ServiceException
	 */
	@Procedure(name = "unCollectUser", level=PrivilegeLevel.LOGGED)
	public String unCollectUser() throws ServiceException {
		try {
			int collectionId = getIntParameter("collectionId");
			UserCollectionDao dao = daofactory.getDao(UserCollectionDao.class);
			int result = dao.uncollect(session.getUser().getId(), collectionId);
			if (result > 0) {
				return JSONUtils.newSuccessfulResult("取消收藏成功");
			} else if (result < 0) {
				return JSONUtils.newFailureResult("不能删除他人的收藏", ErrorCode.AUTH, "");
			} else {
				return JSONUtils.newFailureResult("取消收藏失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause.getMessage(), ErrorCode.wrap(cause), cause.getMessage());
		}
	}
	
	/**
	 * 取消对分享的收藏
	 * @param collectionId int, !nullable
	 * @return 
	 * <pre>
	 * {}
	 * </pre>
	 */
	@Procedure(name = "unCollectShare", level=PrivilegeLevel.LOGGED)
	public String unCollectShare() throws ServiceException {
		try {
			int collectionId = getIntParameter("collectionId");
			ShareCollectionDao dao = daofactory.getDao(ShareCollectionDao.class);
			
			int result = dao.uncollectShare(session.getUser().getId(), collectionId);
			if (result > 0) {
				return JSONUtils.newSuccessfulResult("取消收藏成功");
			} else if (result < 0){ // 不是自己的收藏
				return JSONUtils.newFailureResult("不能删除他人的收藏", ErrorCode.AUTH, "");
			} else {
				return JSONUtils.newFailureResult("取消收藏失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause.getMessage(), ErrorCode.wrap(cause), cause.getMessage());
		}
	}
	
	
	
	private JSONObject packUserCollection(List<UserCollection> collections) {
		JSONArray array = new JSONArray();
		for (UserCollection c : collections) {
			JSONObject item = new JSONObject();
			item.put("collectionId", c.getId());
			item.put("collectTime", c.getCollectTime());
			JSONObject user = new JSONObject();
			user.put("userId", c.getUser().getId());
			user.put("username", c.getUser().getUsername());
			user.put("headImageId", c.getUser().getHeadImage().getId());
			item.put("user", user);
			array.add(item);
		}
		
		JSONObject result = new JSONObject();
		result.put("count", collections.size());
		result.put("collections", array);
		return result;
	}
	
	
	private JSONObject packShareCollection(List<ShareCollection> collections) {
		JSONArray array = new JSONArray();
		for (ShareCollection c : collections) {
			JSONObject item = new JSONObject();
			item.put("collectionId", c.getId());
			item.put("collectTime", c.getCollectTime());
			
			JSONObject share = new JSONObject();
			share.put("shareId", c.getShare().getId());
			share.put("content", c.getShare().getContent());
			share.put("createTime", c.getShare().getCreateTime());
			share.put("praiseCount", c.getShare().getPraiseCount());
			share.put("commentCount", c.getShare().getCommentCount());
			
			item.put("share", share);
			array.add(item);
		}
		JSONObject result = new JSONObject();
		result.put("count", collections.size());
		result.put("collections", array);
		return result;
	}
}
