package cc.lixiaohui.share.dao.mybatis;

import org.junit.Test;

import cc.lixiaohui.share.model.dao.RoleDao;
import cc.lixiaohui.share.model.dao.impl.RoleDaoImpl;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午8:56:02
 */
public class RoleDaoImplTest {
	
	@Test
	public void testList() {
		RoleDao dao = (RoleDao) new RoleDaoImpl();
	}
	
}
