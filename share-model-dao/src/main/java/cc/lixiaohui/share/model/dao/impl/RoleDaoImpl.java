package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;

import cc.lixiaohui.share.model.bean.Role;

/**
 * RoleDao实现
 * @author lixiaohui
 * @date 2016年10月30日 下午8:32:21
 */
public class RoleDaoImpl extends AbstractBaseDao<Role> {

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Role> list() {
		Session session = getSession();
		List<Role> roles = session.createQuery("from Role").list();
		return roles;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Role> listSome(int start, int limit) {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Role getById(int id) {
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
	public int add(Role bean) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(Role bean) {
		return 0;
	}

}
