package cc.lixiaohui.share.dao;

import org.junit.Test;

import cc.lixiaohui.share.model.bean.Picture;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.dao.util.DaoFactory;

public class PictureDaoImplTest {

	@Test
	public void test() throws InstantiationException, IllegalAccessException {
		DaoFactory factory = new DaoFactory();
		PictureDao dao = factory.getDao(PictureDao.class);
		Picture p = new Picture();
		p.setPath("das");
		System.out.println(dao.add(p));
	}
	
}
