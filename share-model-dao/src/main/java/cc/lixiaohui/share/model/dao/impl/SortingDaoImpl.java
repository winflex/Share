package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Sorting;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;
import cc.lixiaohui.share.model.dao.SortingDao;

/**
 * @author lixiaohui
 * @date 2016年11月2日 下午8:23:41
 */
@SuppressWarnings("unchecked")
public class SortingDaoImpl extends SimpleDaoSupport implements SortingDao {

	private static final Logger logger = LoggerFactory.getLogger(SortingDaoImpl.class);
	
	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Sorting> list() {
		Session session = getSession();
		try {
		return session.createQuery("from Sorting").list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Sorting> listSome(int start, int limit) {
		Session session = getSession();
		try {
		return session.createQuery("from Sorting").setFirstResult(start).setMaxResults(limit).list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Sorting getById(int id) {
		Session session = getSession();
		try {
			return (Sorting) getSession().createQuery("from Sorting s where s.id = :sortingId")
					.setParameter("sortingId", id).uniqueResult();
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
		try {
			Sorting s = new Sorting();
			s.setId(id);
			session.delete(s);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			logger.error("error occurred while deleting sorting[id = {}], {}", id, e);
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
	public int add(Sorting bean) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(bean);
			transaction.commit();
			return 1;
		} catch (Exception e) {
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
	public int update(Sorting bean) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.update(bean);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			logger.error("error occurred while persisting {}, {}", bean, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

}
