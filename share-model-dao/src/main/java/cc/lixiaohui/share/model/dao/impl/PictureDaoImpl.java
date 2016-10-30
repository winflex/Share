package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import cc.lixiaohui.share.model.bean.Picture;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:20:33
 */
public class PictureDaoImpl extends SimpleDaoSupport implements PictureDao {

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Picture> list() {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Picture> listSome(int start, int limit) {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Picture getById(int id) {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#delete(int)
	 */
	@Override
	public int delete(int id) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public int add(Picture bean) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(Picture bean) {
		return 0;
	}

}
