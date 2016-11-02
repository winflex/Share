package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.bean.UserCollection;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;
import cc.lixiaohui.share.model.dao.UserCollectionDao;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:22:24
 */
public class UserCollectionDaoImpl extends SimpleDaoSupport implements UserCollectionDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<UserCollection> list() {
		Session session = getSession();
		try{
			return session.createQuery("from user_collecion").list();
		}finally{
			session.close();
		}
		
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<UserCollection> listSome(int start, int limit) {
		Session session = getSession();
		try{
			return session.createQuery("from use_collection").setFirstResult(start)
					.setMaxResults(limit).list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public UserCollection getById(int id) {
		Session session = getSession();
		try {
			return (UserCollection) getSession().createQuery
					("from UserCollection u where u.id = :userCollectionId")
					.setParameter("userCollectionId", id).uniqueResult();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#delete(int)
	 */
	@Override
	public int delete(int id) {
		Session session = getSession();
	    Transaction transaction = session.beginTransaction();
	    try{
	    	UserCollection userCollection=new UserCollection();
	    	userCollection.setId(id);
	        session.delete(userCollection);
	        transaction.commit();
	        return 1;
	    }catch (Exception e) {
			logger.error("error occurred while deleting userCollection[id = {}], {}", id, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public int add(UserCollection bean) {
		Session session = getSession();
	    Transaction transaction = session.beginTransaction();
	    try{
	        session.save(bean);
	        transaction.commit();
	        return 1;
	    }catch (Exception e) {
			logger.error("error occurred while persisting {}, {}", bean, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(UserCollection bean) {
		Session session = getSession();
	    Transaction transaction = session.beginTransaction();
	    try{
	        session.update(bean);
	        transaction.commit();
	        return 1;
	    }catch (Exception e) {
			logger.error("error occurred while persisting {}, {}", bean, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

}
