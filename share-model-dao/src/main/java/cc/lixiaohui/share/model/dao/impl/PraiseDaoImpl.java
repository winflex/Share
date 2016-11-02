package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Praise;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.PraiseDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:21:05
 */
public class PraiseDaoImpl extends SimpleDaoSupport implements PraiseDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Praise> list() {
		Session session = getSession();
		try {
			return session.createQuery("from Praise").list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Praise> listSome(int start, int limit) {
		Session session = getSession();
		try {
			return session.createQuery("from Praise").setFirstResult(start).setMaxResults(limit)
					.list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Praise getById(int id) {
		Session session = getSession();
		try {
			return (Praise) getSession().createQuery("from Praise p where p.id = :praiseId")
					.setParameter("praiseId", id).uniqueResult();
		} finally {
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
			Praise praise=new Praise();
			praise.setId(id);
			session.delete(praise);
			transaction.commit();
			return 1;
		}catch(Exception e){
			logger.error("error occurred while deleting praise[id = {}], {}", id, e);
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
	public int add(Praise bean) {
		Session session=getSession();
		Transaction transaction=session.beginTransaction();
		try{
			session.save(bean);
			transaction.commit();
			return 1;
		}catch (Exception e) {
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
	public int update(Praise bean) {
		Session session=getSession();
		Transaction transaction=session.beginTransaction();
		try{
			session.update(bean);
			transaction.commit();
			return 1;
		}catch (Exception e) {
			logger.error("error occurred while persisting {}, {}", bean, e);
			transaction.rollback();
			return 0;
		} finally {
			session.close();
		}
	}

}
