package cc.lixiaohui.share.server.model.dao;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.server.model.dao.util.DaoException;

/**
 * DAO通用实现
 * 
 * @author lixiaohui
 * @date 2016年11月2日 下午11:19:46
 */
public abstract class AbstractDao<T> extends SimpleDaoSupport implements BaseDao<T> {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);
	
	protected Class<T> entityClass;
	
	protected AbstractDao() {
		this.entityClass = reflectEntityClass();
	}
	
	/**
	 * 通过反射获取T的实际类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private final Class<T> reflectEntityClass() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
			if (actualTypes.length > 0) {
				return (Class<T>) actualTypes[0];
			}
		}
		throw new RuntimeException("cannot reflect T's actual type");
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> list() {
		Session session = getSession();
		try {
			return getSession().createQuery("from " + entityName()).list();
			//return getSession().createQuery(String.format("from %s", entityName())).list();
		} finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<T> list(String hql) {
		Session session = getSession();
		try {
			return getSession().createQuery(hql).list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> listSome(int start, int limit) {
		Session session = getSession();
		try {
			return getSession().createQuery("from " + entityName()).setFirstResult(start).setMaxResults(limit).list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getById(int id) {
		Session session = getSession();
		try {
			return (T) session.get(entityClass, id);
			/*return (T) getSession().createQuery("from " + entityName() + " t where t.id = :id")
					.setParameter("id", id).uniqueResult();*/
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
			session.delete(session.get(entityClass, id));
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
	 * @see cc.lixiaohui.share.model.dao.BaseDao#delete(java.lang.Object)
	 */
	@Override
	public int delete(T t) throws DaoException {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.delete(t);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			logger.error("error occurred while deleting {}", t);
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
	public int add(T bean) {
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
	public int update(T bean) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(bean);
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
	
	protected Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception, NoSuchMethodException {
		Method method = obj.getClass().getMethod(methodName, parameterTypes);
		return method.invoke(obj, parameters);
	}
	
	//protected abstract String mappingTableName();
	protected String entityName() {
		return entityClass.getSimpleName();
	}

}
