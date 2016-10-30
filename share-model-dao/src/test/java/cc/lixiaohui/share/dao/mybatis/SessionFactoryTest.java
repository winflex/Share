package cc.lixiaohui.share.dao.mybatis;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

import cc.lixiaohui.share.model.util.HibernateSessionFactory;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午6:15:59
 */
public class SessionFactoryTest {

	@SuppressWarnings("unused")
	@Test
	public void test() {
		SessionFactory factory = HibernateSessionFactory.getSessionFactory();
		Session session = factory.openSession();
	}

}
