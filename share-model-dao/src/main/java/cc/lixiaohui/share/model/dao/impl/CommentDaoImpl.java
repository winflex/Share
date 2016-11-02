package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Comment;
import cc.lixiaohui.share.model.dao.CommentDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:19:14
 */
public class CommentDaoImpl extends SimpleDaoSupport implements CommentDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Comment> list() {
		Session session = getSession();
		try{
			return session.createQuery("from comment").list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Comment> listSome(int start, int limit) {
		Session session=getSession();
		try{
			return session.createQuery("from comment").setFirstResult(start).setMaxResults(limit)
					.list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Comment getById(int id) {
		Session session=getSession();
		try{
			return (Comment) session.createQuery("from comment c where c := id")
					.setParameter("id", id).uniqueResult();
		}
		finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#delete(int)
	 */
	@Override
	public int delete(int id) {
		Session session=getSession();
		Transaction transaction=session.beginTransaction();
		try{
			Comment comment=new Comment();
			comment.setId(id);
			session.delete(comment);
			transaction.commit();
			return 1;
		}catch(Exception e){
			logger.error("error occurred while deleting comment[id = {}], {}", id, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public int add(Comment bean) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(bean);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			logger.error("error occurred while persisting {}, {}", bean, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(Comment bean) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.update(bean);
			transaction.commit();
			return 1;
		} catch (Exception e) {
			logger.error("error occurred while persisting {}, {}", bean, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

}
