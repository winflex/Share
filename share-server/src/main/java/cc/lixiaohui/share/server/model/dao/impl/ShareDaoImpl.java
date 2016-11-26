package cc.lixiaohui.share.server.model.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import cc.lixiaohui.share.server.model.bean.Comment;
import cc.lixiaohui.share.server.model.bean.Praise;
import cc.lixiaohui.share.server.model.bean.Share;
import cc.lixiaohui.share.server.model.dao.AbstractDeleteableDao;
import cc.lixiaohui.share.server.model.dao.ShareDao;
import cc.lixiaohui.share.server.model.dao.util.DaoException;

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

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Share> list(int currentUserId, int targetUserId, String keyword, long baseTime, boolean deleted, String orderColumn, String orderType, int start, int limit) throws DaoException {
		// where condition include targetUserId, keyword, baseTime, deleted
		Session session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append("select s from Share s join s.publisher p where ");
		
		hql.append("s.content like :keyword ");
		params.put("keyword", keyword);
		hql.append("and s.deleted = :deleted ");
		params.put("deleted", deleted);
		if (targetUserId > -1) { // specified user
			hql.append("and p.id = :targetUserId ");
			params.put("targetUserId", targetUserId);
		}
		if (baseTime > 0) { // based on specific time
			hql.append("and s.createTime > :createTime ");
			params.put("createTime", new Date(baseTime));
		}
		
		if (currentUserId > 0) { // 已登陆, 此时当分享不是自己发布的时候, 若发布者被屏蔽了, 则该分享对当前用户不可见
			hql.append("and (p.id = :currentUserId or (p.id != :currentUserId and p.selfForbid = false and p.adminForbid = false)) ");
			params.put("currentUserId", currentUserId);
		} else {
			// 未登陆, 此时若分享的发布者被屏蔽了, 则该分享对当前用户不可见
			hql.append("and p.selfForbid = false and p.adminForbid = false ");
		}
		
		hql.append("order by s.").append(orderColumn).append(" ").append(orderType).append(" ");
		
		List<Share> shares = session.createQuery(hql.toString())
				.setProperties(params)
				.setFirstResult(start)
				.setMaxResults(limit).list();
		return shares;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listLikedByUser(int userId, int start, int limit) throws DaoException {
		return getSession().createSQLQuery("select {s.*}, {p.*} "
				+ "from share s join praise p on s.id = p.share_id "
				+ "where p.user_id = :userId order by p.praise_time desc")
				.addEntity("s", Share.class)
				.addEntity("p", Praise.class)
				.setParameter("userId", userId)
				.setFirstResult(start)
				.setMaxResults(limit).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listCommentedByUser(int userId, int start, int limit) throws DaoException {
		return getSession().createSQLQuery("select {s.*}, {c.*} "
				+ "from share s join comment c on s.id = c.share_id "
				+ "where c.from_user_id = :userId order by c.comment_time desc")
			.addEntity("s", Share.class)
			.addEntity("c", Comment.class)
			.setParameter("userId", userId)
			.setFirstResult(start)
			.setMaxResults(limit).list();
	}
}
