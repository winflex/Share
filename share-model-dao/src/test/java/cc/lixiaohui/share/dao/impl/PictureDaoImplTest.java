package cc.lixiaohui.share.dao.impl;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.Picture;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.util.DaoFactory;

public class PictureDaoImplTest {

	@Test
	public void test() throws Exception {
		DaoFactory factory = new DaoFactory();
		PictureDao dao = factory.getDao(PictureDao.class);
		Picture p = new Picture();
		p.setPath("das");
		System.out.println(dao.add(p));
	}
	
}
