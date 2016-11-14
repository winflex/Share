package cc.lixiaohui.share.model.dao;

import java.util.List;

import cc.lixiaohui.share.model.bean.FriendShip;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年11月2日 下午11:17:57
 */
public interface FriendShipDao extends BaseDao<FriendShip> {
	
	public List<?> getFriends(int userId, int start, int limit) throws DaoException;
	
	
	public int deleteFriend(int userId, int friendId) throws DaoException;
	
}
