package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cc.lixiaohui.share.model.bean.FriendShip;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.AbstractDao;
import cc.lixiaohui.share.model.dao.FriendShipDao;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年11月2日 下午11:18:59
 */
public class FriendShipDaoImpl extends AbstractDao<FriendShip> implements FriendShipDao {
	
	private static final String SQL_FRIENDS;
	static {
		StringBuilder sb  = new StringBuilder();
		sb.append("select fs.*, u.* from friendship fs join user u on fs.asked_user_id = u.id where fs.ask_user_id = %d and pending = %s");
		sb.append(" union ");
		sb.append("select fs.*, u.* from friendship fs join user u on fs.ask_user_id = u.id where fs.asked_user_id = %d and pending = %s");	
		SQL_FRIENDS = sb.toString();
	}
	
	private static String friendSQL(int userId, boolean pending) {
		String p = pending ? "'1'" : "'0'";
		return String.format(SQL_FRIENDS, userId, p, userId, p);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getFriends(int userId, int start, int limit) throws DaoException {
		Session session = getSession();
		return session.createSQLQuery(friendSQL(userId, false)).addEntity("fs", FriendShip.class).addEntity("u", User.class).list();
	}
	
	@Override
	public int deleteFriend(int userId, int friendShipId) throws DaoException {
		Session session = getSession();
		Transaction trans = session.beginTransaction();
		try {
			FriendShip friendShip = (FriendShip) session.get(FriendShip.class, friendShipId);
			int result = 0;
			if (friendShip.getAskUser().getId() == userId || friendShip.getAskedUser().getId() == userId) {
				result = session.createQuery("delete from FriendShip fs where fs.id = :id").setParameter("id", friendShipId).executeUpdate();
			} else {
				result = 0;
			}
			trans.commit();
			return result;
		} catch (Exception e) {
			logger.error("{}", e);
			trans.rollback();
			throw new DaoException(e);
		} finally {
			session.close();
		}
	}

	
}
