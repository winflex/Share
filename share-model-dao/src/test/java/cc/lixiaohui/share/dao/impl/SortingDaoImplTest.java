package cc.lixiaohui.share.dao.impl;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.Sorting;
import cc.lixiaohui.share.model.dao.SortingDao;
import cc.lixiaohui.share.model.util.DaoFactory;

/**
 * @author lixiaohui
 * @date 2016年11月2日 下午8:31:36
 */
public class SortingDaoImplTest {
	
	@Test
	public void test() throws Exception {
		SortingDao dao = new DaoFactory().getDao(SortingDao.class);
		Sorting s = new Sorting();
		s.setType("赞");
		s.setDescription("按点赞数排序");
		System.out.println(dao.add(s));
	}
	
}	
