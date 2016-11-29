package cc.lixiaohui.share.server.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import cc.lixiaohui.share.server.model.bean.Picture;
import cc.lixiaohui.share.server.model.dao.AbstractDao;
import cc.lixiaohui.share.server.model.dao.PictureDao;
import cc.lixiaohui.share.server.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:20:33
 */
public class PictureDaoImpl extends AbstractDao<Picture> implements PictureDao {

	static final String EMPTY_STRING = "";
	
	static final List<Picture> EMPTY_LIST = new ArrayList<Picture>();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Picture> getPictures(int[] ids) throws DaoException {
		Session session = getSession();
		String idString = concatId(ids);
		if (EMPTY_STRING.equals(idString)) {
			return EMPTY_LIST;
		} else {
			return session.createQuery("from Picture p where p.id in " + "(" + concatId(ids) +")").list();
		}
		
	}

	
	private String concatId(int[] ids) {
		StringBuilder sb = new StringBuilder();
		for (int id : ids) {
			sb.append(id).append(',');
		}
		if (ids.length > 0) {
			return sb.subSequence(0, sb.length() - 1).toString();
		}
		return sb.toString();
	}
}
