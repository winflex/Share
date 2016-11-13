package cc.lixiaohui.share.model.dao;

import java.util.List;

/**
 * 通用Dao基类
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午11:10:08
 */
public interface BaseDao<T>{
	
	/**
	 * 返回所有实体
	 * @return
	 */
	List<T> list() throws DaoException;
	
	/**
	 * 返回start ~ limit间的所有实体
	 * @param start 起始(inclusive)
	 * @param limit 结束(exclusive)
	 * @return
	 */
	List<T> listSome(int start, int limit) throws DaoException;
	
	/**
	 * 根据id获取实体
	 * @param id
	 * @return
	 */
	T getById(int id) throws DaoException;
	
	/**
	 * 根据id删除实体
	 * @param id
	 * @return
	 */
	int delete(int id) throws DaoException;
	
	int delete(T t) throws DaoException;
	
	/**
	 * 添加实体
	 * @param bean
	 * @return
	 */
	int add(T bean) throws DaoException;
	
	/**
	 * 更新实体
	 * @param bean
	 * @return
	 */
	int update(T bean) throws DaoException;
}
