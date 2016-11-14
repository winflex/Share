package cc.lixiaohui.share.model.dao.impl;

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
}
