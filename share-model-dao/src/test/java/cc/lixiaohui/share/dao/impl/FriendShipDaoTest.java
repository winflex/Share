package cc.lixiaohui.share.dao.impl;

import java.util.List;

import org.junit.Test;

import cc.lixiaohui.share.model.dao.FriendShipDao;
import cc.lixiaohui.share.model.util.DaoFactory;

import com.alibaba.fastjson.JSON;

/**
 * @author lixiaohui
 * @date 2016年11月12日 下午4:13:36
 */
public class FriendShipDaoTest {
	
	DaoFactory factory = new DaoFactory();
	
	@Test
	public void testFriends() throws Exception {
		FriendShipDao dao = factory.getDao(FriendShipDao.class);
		List<?> list = dao.getFriends(5, 0, 2);
		String json = JSON.toJSONString(list);
		System.out.println(json);
	}
	@Test
	public void testDeleteFriend() throws Exception {
		FriendShipDao dao = factory.getDao(FriendShipDao.class);
		int a = dao.deleteFriend(5, 4);
		System.out.println(a);
	}
	
}
