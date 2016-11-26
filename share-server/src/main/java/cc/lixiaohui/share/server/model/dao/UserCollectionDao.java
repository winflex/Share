package cc.lixiaohui.share.server.model.dao;

import java.util.List;

import cc.lixiaohui.share.server.model.bean.UserCollection;
import cc.lixiaohui.share.server.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午5:03:50
 */
public interface UserCollectionDao extends BaseDao<UserCollection> {
	
	List<UserCollection> getByUserId(int userId, int start, int limit) throws DaoException;
	
	int uncollect(int userId, int collectionId) throws DaoException;
}
