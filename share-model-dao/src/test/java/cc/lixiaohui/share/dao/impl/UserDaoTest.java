package cc.lixiaohui.share.dao.impl;

import java.util.List;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.model.util.DaoFactory;

public class UserDaoTest {
	DaoFactory factory = new DaoFactory();
	
	@Test
	public void testAdd() throws Exception {
		UserDao dao = factory.getDao(UserDao.class);
		System.out.println(dao.add(simpleUser()));
	}
	@Test
	public void testDelete() throws Exception{
		UserDao dao = factory.getDao(UserDao.class);
		System.out.println(dao.delete(6));
	}
	
	@Test
	public void testList() throws Exception{
		UserDao dao = factory.getDao(UserDao.class);
		List<User> users = dao.list();
		for (User user : users) {
			System.out.println(user);
		}
	}
	@Test
	public void testGetUser() throws Exception {
		UserDao dao = factory.getDao(UserDao.class);
		User user = dao.get("小明", "xiaoming");
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
	}
	
	@Test
	public void testFriends() throws Exception {
		UserDao dao = factory.getDao(UserDao.class);
		User xiaoming = dao.getById(5);
		System.out.println(xiaoming);
		List<User> friends = xiaoming.getFriends();
		System.out.println(friends);
		for (User fri : friends) {
			System.out.println(fri);
		}
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
