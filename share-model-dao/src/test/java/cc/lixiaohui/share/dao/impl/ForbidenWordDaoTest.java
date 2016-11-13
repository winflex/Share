package cc.lixiaohui.share.dao.impl;

import java.util.List;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.ForbidenWord;
import cc.lixiaohui.share.model.dao.ForbidenWordDao;
import cc.lixiaohui.share.model.util.DaoFactory;

/**
 * @author lixiaohui
 * @date 2016年11月12日 下午5:54:47
 */
public class ForbidenWordDaoTest {
	
	DaoFactory factory = new DaoFactory();
	
	@Test
	public void list() throws Exception {
		ForbidenWordDao dao = factory.getDao(ForbidenWordDao.class);
		List<ForbidenWord> words = dao.list();
		for (ForbidenWord word : words) {
			System.out.println(word);
		}
	}
	@Test
	public void add()  throws Exception {
		ForbidenWordDao dao = factory.getDao(ForbidenWordDao.class);
		ForbidenWord word = new ForbidenWord();
		word.setContent("法西斯");
		int result = dao.add(word);
		System.out.println(result);
	}
}
