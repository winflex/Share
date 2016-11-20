package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cc.lixiaohui.share.model.bean.ShareCollection;
import cc.lixiaohui.share.model.dao.AbstractDeleteableDao;
import cc.lixiaohui.share.model.dao.ShareCollectionDao;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:21:44
 */
public class ShareCollectionDaoImpl extends AbstractDeleteableDao<ShareCollection> implements ShareCollectionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareCollection> getByUserId(int userId, int start, int limit) throws DaoException {
		Session session = getSession();
		return session.createQuery("select sc from ShareCollection sc join sc.user u where u.id = :userId").setParameter("userId", userId)
			.setFirstResult(start).setMaxResults(limit).list();
	}

	@Override
	public int uncollectShare(int userId, int collectionId) throws DaoException {
		Session session = getSession();
		Transaction trans = session.beginTransaction();
		try {
			ShareCollection collection = (ShareCollection) session.get(ShareCollection.class, collectionId);
			int result = 0;
			if (collection.getUser().getId() == userId) {
				result = session.createQuery("delete from ShareCollection sc where sc.id = :collectionId").setParameter("collectionId", collection.getId()).executeUpdate();
			} else {
				result = -1;
			}
			trans.commit();
			return result;
		} finally {
			session.close();
		}
	}
	
}
