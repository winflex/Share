package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;
import cc.lixiaohui.share.model.dao.UserDao;


/**
 * UserDao实现
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午7:51:22
 */
public class UserDaoImpl extends SimpleDaoSupport implements UserDao{

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<User> list() {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<User> listSome(int start, int limit) {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public User getById(int id) {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#delete(int)
	 */
	@Override
	public int delete(int id) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public int add(User bean) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(User bean) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.UserDao#get(java.lang.String, java.lang.String)
	 */
	@Override
	public User get(String username, String password) {
		return null;
	}

	

}
