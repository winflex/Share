package cc.lixiaohui.share.server.model.dao.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Dao资源提供类
 * 
 * @author lixiaohui
 * @date 2016年10月30日 下午8:26:41
 */
public interface DaoSupport {
	
	/**
	 * 获取新的hibernate session
	 * @return
	 */
	Session getSession();
	
	SessionFactory getSessionFactory();
	
}
