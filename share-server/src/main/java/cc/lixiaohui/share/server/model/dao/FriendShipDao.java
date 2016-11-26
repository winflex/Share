package cc.lixiaohui.share.server.model.dao;

import java.util.List;

import cc.lixiaohui.share.server.model.bean.FriendShip;
import cc.lixiaohui.share.server.model.dao.util.DaoException;


/**
 * @author lixiaohui
 * @date 2016年11月2日 下午11:17:57
 */
public interface FriendShipDao extends BaseDao<FriendShip> {
	
	public List<Object[]> getFriends(int userId, int start, int limit) throws DaoException;
	
	
	public int deleteFriend(int userId, int friendShipId) throws DaoException;
	
}
