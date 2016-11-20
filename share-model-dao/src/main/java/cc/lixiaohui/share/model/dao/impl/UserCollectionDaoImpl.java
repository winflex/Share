package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cc.lixiaohui.share.model.bean.UserCollection;
import cc.lixiaohui.share.model.dao.AbstractDao;
import cc.lixiaohui.share.model.dao.UserCollectionDao;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:22:24
 */
public class UserCollectionDaoImpl extends AbstractDao<UserCollection> implements UserCollectionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<UserCollection> getByUserId(int userId, int start, int limit) throws DaoException {
		Session session = getSession();
		return session.createQuery("select uc from UserCollection uc join uc.user u where u.id = :userId").setParameter("userId", userId)
				.setFirstResult(start).setMaxResults(limit).list();
	}

	@Override
	public int uncollect(int userId, int collectionId) throws DaoException {
		Session session = getSession();
		Transaction trans = session.beginTransaction();
		try {
			UserCollection collection = (UserCollection) session.get(UserCollection.class, collectionId);
			int result = 0;
			if (collection.getUser().getId() == userId) {
				result = session.createQuery("delete from UserCollection uc where uc.id = :collectionId").setParameter("collectionId", collection.getId()).executeUpdate();
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
