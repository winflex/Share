package cc.lixiaohui.share.model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 带有deleted的字段的DAO可继承该类
 * @author lixiaohui
 * @date 2016年11月12日 上午12:40:37
 */
public abstract class AbstractDeleteableDao<T> extends AbstractDao<T> implements DeleteableDao<T>{
	
	/**
	 * 列出所有未被删除(逻辑层面)的tuple
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> listUndeleted() {
		Session session = getSession();
		try {
			return session.createQuery("from " + entityName() + " where deleted = 0").list();
		} finally {
			session.close();
		}
	}
	
	/**
	 * 列出部分未被删除(逻辑层面)的tuple
	 * @param start 起始条数
	 * @param limit 总条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> listSomeUndeleted(int start, int limit) {
		Session session = getSession();
		try {
			return getSession().createQuery("from " + entityName() + " where deleted = 0")
					.setFirstResult(start).setMaxResults(limit).list();
		} finally {
			session.close();
		}
	}
	
	/**
	 * 列出部分已被删除(逻辑层面)的tuple
	 * @param start 起始条数
	 * @param limit 总条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> listSomeDeleted(int start, int limit) {
		Session session = getSession();
		try {
			return getSession().createQuery("from " + entityName() + " where deleted = 1")
					.setFirstResult(start).setMaxResults(limit).list();
		} finally {
			session.close();
		}
	}
	
	
	
	/**
	 * 逻辑删除tuple, 即设deleted标志为1
	 * @param id
	 * @return The number of entities updated or deleted
	 * @throws DaoException 
	 */
	public int vitualDelete(int id) throws DaoException {
		Session session = getSession();
		Transaction tran = session.beginTransaction();
		try {
			T entity = getById(id);
			invokeMethod(entity, "setDeleted", new Class<?>[]{boolean.class}, new Object[]{true});
			session.update(entity);
			tran.commit();
			return 1;
		} catch (Exception e) {
			logger.error("{}", e);
			tran.rollback();
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}
	
	/**
	 * 是 {@link #vitualDelete(int)}的逆过程
	 * @param id
	 * @return The number of entities updated or deleted
	 * @throws DaoException 
	 */
	public int recover(int id) throws DaoException {
		Session session = getSession();
		Transaction tran = session.beginTransaction();
		try {
			T entity = getById(id);
			invokeMethod(entity, "setDeleted", new Class<?>[]{boolean.class}, new Object[]{false});
			session.update(entity);
			tran.commit();
			return 1;
		} catch (Exception e) {
			logger.error("{}", e);
			tran.rollback();
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}
}
