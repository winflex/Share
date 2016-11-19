package cc.lixiaohui.share.model.dao.impl;


import java.util.List;

import org.hibernate.Session;

import cc.lixiaohui.share.model.bean.Comment;
import cc.lixiaohui.share.model.dao.AbstractDeleteableDao;
import cc.lixiaohui.share.model.dao.CommentDao;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:19:14
 */
public class CommentDaoImpl extends AbstractDeleteableDao<Comment> implements CommentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Comment> getByShareId(int shareId, int start, int limit) throws DaoException {
		Session session = getSession();
		return session.createQuery("select cs from Share s right join s.comments cs where s.id = :shareId")
				.setParameter("shareId", shareId).setFirstResult(start).setMaxResults(limit).list();
	}

}
