package cc.lixiaohui.share.dao;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.model.dao.util.DaoFactory;

public class UserDaoTest {
	
	@Test
	public void test() throws InstantiationException, IllegalAccessException {
		DaoFactory factory = new DaoFactory();
		UserDao dao = factory.getDao(UserDao.class);
		System.out.println(dao.add(simpleUser()));
	}

	private User simpleUser() {
		Role role= new Role();
		role.setId(1);
		
		User user = new User();
		user.setUsername("lixiaohui");
		user.setPassword("dads");
		user.setSex("男");
		user.setSignature("你好");
		user.setRole(role);
		return user;
	}
	
}
