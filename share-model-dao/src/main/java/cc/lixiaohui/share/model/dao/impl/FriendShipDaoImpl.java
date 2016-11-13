package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cc.lixiaohui.share.model.bean.FriendShip;
import cc.lixiaohui.share.model.dao.AbstractDao;
import cc.lixiaohui.share.model.dao.DaoException;
import cc.lixiaohui.share.model.dao.FriendShipDao;

/**
 * @author lixiaohui
 * @date 2016年11月2日 下午11:18:59
 */
public class FriendShipDaoImpl extends AbstractDao<FriendShip> implements FriendShipDao {
	
	private static final String SQL_FRIEND_LIST = "select * from (select fs0.id relationId, u1.id userId, u1.username, u1.role_id roleId, u1.sex, u1.signature, u1.register_time registerTime, u1.head_image_id headImageId, fs0.answer_time answerTime from user u0 join friendship fs0 on u0.id = fs0.ask_user_id join user u1 on u1.id = fs0.asked_user_id where u0.id = :whose and fs0.pending = 0 union select  fs1.id relationId, u3.id userId, u3.username, u3.role_id roleId, u3.sex, u3.signature, u3.register_time registerTime, u3.head_image_id headImageId, fs1.answer_time answerTime from user u2 join friendship fs1 on u2.id = fs1.asked_user_id join user u3 on u3.id = fs1.ask_user_id where u2.id = :whose and fs1.pending = 0) as r order by registerTime limit :start,:limit"; 
	private static final String SQL_FRIEND_DEL = "delete from friendship where (ask_user_id = :userId and asked_user_id = :friendId) or (ask_user_id = :friendId and asked_user_id = :userId)"; 
	
	private static final String HQL_FRIEND_DEL = "delete FriendShip fs where (fs.askUser = :userId and fs.askedUser = :friendId) or (fs.askUser = :friendId and fs.askedUser = :userId)";
	
	public List<?> getFriends(int userId, int start, int limit) throws DaoException{
		Session session = getSession();
		try {
			return session.createSQLQuery(SQL_FRIEND_LIST)
			.setParameter("whose", userId)
			.setParameter("start", start)
			.setParameter("limit", limit).list();
		} catch (Exception e) {
			logger.error("{}", e);
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}

	@Override
	public int deleteFriend(int userId, int friendId) throws DaoException {
		Session session = getSession();
		Transaction trans = session.beginTransaction();
		try {
			FriendShip fs = (FriendShip) session.createQuery("from FriendShip fs where askUser.id = 4").uniqueResult();
					//.setInteger("userId", userId)
					//.setInteger("friendId", friendId).uniqueResult();
			
			session.delete(fs);
			/*int result = session.createSQLQuery(HQL_FRIEND_DEL)
					.setInteger("friendId", friendId)
					.setInteger("userId", userId).executeUpdate();
			trans.commit();*/
			return 1;
		} catch (Exception e) {
			logger.error("{}", e);
			trans.rollback();
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}

	
}
