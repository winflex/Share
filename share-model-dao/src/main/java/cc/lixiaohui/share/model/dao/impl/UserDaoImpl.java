package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.model.util.HibernateSessionFactory;


/**
 * UserDao实现
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午7:51:22
 */
public class UserDaoImpl extends SimpleDaoSupport implements UserDao{
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<User> list() {
		Session session = getSession();
		try {
			return session.createQuery("from User").list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<User> listSome(int start, int limit) {
		Session session = getSession();
		try {
			return getSession().createQuery("from User").setFirstResult(start)
					.setMaxResults(limit).list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public User getById(int id) {
		Session session = getSession();
		try {
			return (User) getSession().createQuery("from User u where u.id = :userId")
					.setParameter("userId", id).uniqueResult();
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
			User user = new User();
			user.setId(id);
			session.delete(user);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			logger.error("error occurred while deleting user[id = {}], {}", id, e);
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
	public int add(User bean) {
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
	public int update(User bean) {
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

	/* 
	 * @see cc.lixiaohui.share.model.dao.UserDao#get(java.lang.String, java.lang.String)
	 */
	@Override
	public User get(String username, String password) {
		Session session = getSession();
		try {
			return (User) getSession().createQuery("from User u where u.username = :username "
					+ "AND u.password = :password")
					.setParameter("username",username ).uniqueResult();
			
			
		} finally {
			session.close();
		}
	}

	

}
