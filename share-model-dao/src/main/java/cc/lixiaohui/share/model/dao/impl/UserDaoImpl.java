package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;

import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.AbstractDeleteableDao;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.model.dao.util.DaoException;


/**
 * UserDao实现
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午7:51:22
 */
public class UserDaoImpl extends AbstractDeleteableDao<User> implements UserDao {

	@Override
	public User get(String username, String password) throws DaoException{
		Session session = getSession();
		try {
			return (User) session.createQuery("from User where username = :username and password = :password")
					.setParameter("username", username)
					.setParameter("password", password).uniqueResult();
		} catch (Exception e) {
			logger.error("{}", e);
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}
	
	@Override
	public boolean nameExist(String username) throws DaoException {
		Session session = getSession();
		try {
			long n = (Long) session.createQuery("select count(*) from User where username = :username").setParameter("username", username).uniqueResult();
			return n > 0;
		} catch (Exception e) {
			logger.error("{}", e);
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> search(String keyword, int start, int limit) throws DaoException {
		Session session = getSession();
		try {
			return session.createQuery("from User u where u.username like :keyword")
					.setParameter("keyword", keyword).setFirstResult(start).setMaxResults(limit).list();
		} catch (Exception e) {
			logger.error("{}", e);
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}
}
