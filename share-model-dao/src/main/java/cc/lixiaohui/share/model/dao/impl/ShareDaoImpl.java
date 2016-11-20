package cc.lixiaohui.share.model.dao.impl;

import java.util.Date;
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
	public List<Share> list(int currentUserId, String keyword, long baseTime, boolean deleted, String orderColumn, String orderType, int start, int limit) throws DaoException {
		Session session = getSession();
		
		if (currentUserId > -1) { // TODO 已登陆的会话, 要把别人的selfForbid和adminForbid为true的share去掉
			if (baseTime > 0 ) {
				return session.createQuery("select s from Share s join s.publisher p "
						+ "where (p.id = :userId or (p.id != :userId and p.selfForbid = false and p.adminForbid = false)) and  s.content like :keyword and s.deleted = :deleted  and s.createTime > :baseTime "
						+ "order by s." + orderColumn + " " + orderType)
						.setParameter("keyword", keyword).setParameter("deleted", deleted)
						.setTimestamp("baseTime", new Date(baseTime)).setParameter("userId", currentUserId)
						.setFirstResult(start).setMaxResults(limit).list();
			} else {
				return session.createQuery("select s from Share s join s.publisher p "
						+ "where (p.id = :userId or (p.id != :userId and p.selfForbid = false and p.adminForbid = false)) and s.content like :keyword and s.deleted = :deleted  "
						+ "order by s." + orderColumn + " " + orderType)
						.setParameter("keyword", keyword).setParameter("deleted", deleted)
						.setParameter("userId", currentUserId)
						.setFirstResult(start).setMaxResults(limit).list();
			}
		} else { // TODO 未登陆的会话需要考虑selfShield和adminShield的情况
			if (baseTime > 0 ) {
				return session.createQuery("select s from Share s join s.publisher p "
						+ "where p.selfForbid = false and p.adminForbid = false and s.content like :keyword and s.deleted = :deleted and s.createTime > :baseTime "
						+ "order by s." + orderColumn + " " + orderType)
						.setParameter("keyword", keyword).setParameter("deleted", deleted).setTimestamp("baseTime", new Date(baseTime))
						.setFirstResult(start).setMaxResults(limit).list();
			} else {
				return session.createQuery("select s from Share s join s.publisher p "
						+ "where p.selfForbid = false and p.adminForbid = false and s.content like :keyword and s.deleted = :deleted "
						+ "order by s." + orderColumn + " " + orderType)
						.setParameter("keyword", keyword).setParameter("deleted", deleted)
						.setFirstResult(start).setMaxResults(limit).list();
			}
		}
	}

}
