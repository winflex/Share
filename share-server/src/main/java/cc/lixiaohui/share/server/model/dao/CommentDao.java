package cc.lixiaohui.share.server.model.dao;

import java.util.List;

import cc.lixiaohui.share.server.model.bean.Comment;
import cc.lixiaohui.share.server.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午5:00:34
 */
public interface CommentDao extends DeleteableDao<Comment> {
	
	List<Comment> getByShareId(int shareId, int start, int limit) throws DaoException;
}
