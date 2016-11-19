package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Session;

import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.dao.AbstractDeleteableDao;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:30:28
 */
public class ShareDaoImpl extends AbstractDeleteableDao<Share> implements ShareDao {

	@Override
	public Share getByIdForDetail(int shareId) {
		Session session = getSession();
		Share share = (Share) session.load(Share.class, shareId);
		if (share == null) {
			return null;
		}
		share.getComments(); // load comments
		share.getPraises(); // load praises
		return share;
	}

	@Override
	public Share getByIdForComment(int shareId) {
		Session session = getSession();
		Share share = (Share) session.load(Share.class, shareId);
		if (share == null) {
			return null;
		}
		share.getComments(); // load comments
		return share;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Share> list(String keyword, long baseTime, boolean deleted, String orderColumn, String orderType, int start, int limit) throws DaoException {
		Session session = getSession();
		return session.createQuery("from Share where content like :keyword and deleted = :deleted order by " + orderColumn + " " + orderType)
			.setParameter("keyword", keyword).setParameter("deleted", deleted)
			.setFirstResult(start).setMaxResults(limit).list();
	}

}
