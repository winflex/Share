package cc.lixiaohui.share.dao.impl;

import java.util.List;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.Picture;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.util.DaoFactory;

/**
 * @author lixiaohui
 * @date 2016年11月12日 下午1:41:02
 */
public class PictureDaoTest {
	
	DaoFactory factory = new DaoFactory();
	
	@Test
	public void testList() throws Exception {
		PictureDao dao = factory.getDao(PictureDao.class);
		List<Picture> pictures = dao.list();
		for (Picture p : pictures) {
			System.out.println(p);
		}
	}
	
	
}
