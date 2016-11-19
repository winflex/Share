package cc.lixiaohui.share.server.service;

import java.util.Map;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;

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
	@Procedure(name = "getUserCollection")
	public String getUserCollection() throws ServiceException {
		
		return null;
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
	@Procedure(name = "getShareCollection")
	public String getShareCollection() throws ServiceException {
		return null;
	}
	
	/**
	 * <pre>
	 * int userId
	 * {}
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "unCollectUser")
	public String unCollectUser() throws ServiceException {
		return null;
	}
	
	/**
	 * <pre>
	 * String unCollectionShare
	 * {}
	 * </pre>
	 */
	@Procedure(name = "unCollectionShare")
	public String unCollectionShare() throws ServiceException {
		return null;
	}
}
