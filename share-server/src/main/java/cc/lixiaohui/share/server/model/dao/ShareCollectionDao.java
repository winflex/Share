package cc.lixiaohui.share.server.model.dao;

import java.util.List;

import cc.lixiaohui.share.server.model.bean.ShareCollection;
import cc.lixiaohui.share.server.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午5:03:28
 */
public interface ShareCollectionDao extends BaseDao<ShareCollection> {
	List<ShareCollection> getByUserId(int userId, int start, int limit) throws DaoException;
	
	int uncollectShare(int userId, int collectionId) throws DaoException;
}
