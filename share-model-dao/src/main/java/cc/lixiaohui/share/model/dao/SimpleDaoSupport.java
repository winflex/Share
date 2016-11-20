package cc.lixiaohui.share.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cc.lixiaohui.share.model.dao.util.DaoSupport;
import cc.lixiaohui.share.model.util.HibernateSessionFactory;

/**
 * BaseDao抽象实现, 提供hibernate session支持
 * 
 * @author lixiaohui
 * @date 2016年10月30日 下午8:26:11
 */
public class SimpleDaoSupport implements DaoSupport {
	
	@Override
	public Session getSession() {
		return HibernateSessionFactory.getSessionFactory().openSession();
	}

	@Override
	public SessionFactory getSessionFactory() {
		return HibernateSessionFactory.getSessionFactory();
	}
	
}
