package cc.lixiaohui.share.model.dao;

import java.util.List;

import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * 用户DAO
 * 除了listDeleted外其他list方法都不会select出deleted = 1的记录
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午6:28:42
 */
public interface UserDao extends DeleteableDao<User>{
	
	/**
	 * 根据username, password获取用户
	 * @param username
	 * @param password
	 * @return 若无对应user返回null, 否则返回对应用户
	 * @throws DaoException 
	 */
	User get(String username, String password) throws DaoException;
	
	boolean nameExist(String username) throws DaoException;
	
	List<User> search(String keyword, int start, int limit) throws DaoException;
	
}
