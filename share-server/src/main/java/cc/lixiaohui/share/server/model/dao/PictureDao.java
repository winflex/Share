package cc.lixiaohui.share.server.model.dao;

import java.util.List;

import cc.lixiaohui.share.server.model.bean.Picture;
import cc.lixiaohui.share.server.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午5:01:51
 */
public interface PictureDao extends BaseDao<Picture> {
	
	public List<Picture> getPictures(int[] ids) throws DaoException;
	
}
