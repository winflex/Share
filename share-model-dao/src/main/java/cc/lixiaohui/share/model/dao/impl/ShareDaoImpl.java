package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:30:28
 */
public class ShareDaoImpl extends SimpleDaoSupport implements ShareDao {

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Share> list() {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Share> listSome(int start, int limit) {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Share getById(int id) {
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
	public int add(Share bean) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(Share bean) {
		return 0;
	}

}
