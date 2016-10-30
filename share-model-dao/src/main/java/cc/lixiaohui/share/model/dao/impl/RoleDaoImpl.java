package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.dao.RoleDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * RoleDao实现
 * 
 * @author lixiaohui
 * @date 2016年10月30日 下午8:32:21
 */
@SuppressWarnings("unchecked")
public class RoleDaoImpl extends SimpleDaoSupport implements RoleDao {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	
	@Override
	public List<Role> list() {
		Session session = getSession();
		try {
			return session.createQuery("from Role").list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Role> listSome(int start, int limit) {
		Session session = getSession();
		try {
			return getSession().createQuery("from Role").setFirstResult(start)
					.setMaxResults(limit).list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Role getById(int id) {
		Session session = getSession();
		try {
			return (Role) getSession().createQuery("from Role r where r.id = :roleId")
					.setParameter("roleId", id).uniqueResult();
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
			Role role = new Role();
			role.setId(id);
			session.delete(role);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			logger.error("error occurred while deleting role[id = {}], {}", id, e);
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
	public int add(Role bean) {
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
	public int update(Role bean) {
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
