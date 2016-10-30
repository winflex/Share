package cc.lixiaohui.share.model.dao.impl;

import org.hibernate.Session;

import cc.lixiaohui.share.model.dao.BaseDao;
import cc.lixiaohui.share.model.dao.DaoSupport;
import cc.lixiaohui.share.model.util.HibernateSessionFactory;

/**
 * BaseDao抽象实现, 提供hibernate session支持
 * 
 * @author lixiaohui
 * @date 2016年10月30日 下午8:26:11
 */
public abstract class AbstractBaseDao<T> implements BaseDao<T>, DaoSupport {
	
	/* 
	 * @see cc.lixiaohui.share.model.dao.DaoSupport#getSession()
	 */
	@Override
	public Session getSession() {
		return HibernateSessionFactory.getSessionFactory().openSession();
	}
	
}
