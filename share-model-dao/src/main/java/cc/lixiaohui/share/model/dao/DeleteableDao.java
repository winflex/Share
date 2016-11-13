package cc.lixiaohui.share.model.dao;

import java.util.List;

/**
 * @author lixiaohui
 * @date 2016年11月12日 下午12:04:46
 */
public interface DeleteableDao<T> extends BaseDao<T> {
	
	public List<T> listUndeleted() throws DaoException;
	
	public List<T> listSomeUndeleted(int start, int limit) throws DaoException;
	
	public int vitualDelete(int id) throws DaoException;
	
	public int recover(int id) throws DaoException;
	
	public List<T> listSomeDeleted(int start, int limit) throws DaoException;
}