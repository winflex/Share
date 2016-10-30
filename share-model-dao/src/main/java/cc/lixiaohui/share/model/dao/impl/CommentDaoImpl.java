package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import cc.lixiaohui.share.model.bean.Comment;
import cc.lixiaohui.share.model.dao.CommentDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:19:14
 */
public class CommentDaoImpl extends SimpleDaoSupport implements CommentDao {

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Comment> list() {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Comment> listSome(int start, int limit) {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Comment getById(int id) {
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
	public int add(Comment bean) {
		return 0;
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(Comment bean) {
		return 0;
	}

}
