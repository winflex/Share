package cc.lixiaohui.share.model.dao.impl;

import java.util.List; 

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.ForbidenWord;
import cc.lixiaohui.share.model.dao.ForbidenWordDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:19:58
 */
@SuppressWarnings("unchecked")
public class ForbidenWordDaoImpl extends SimpleDaoSupport implements ForbidenWordDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<ForbidenWord> list() {
		Session session=getSession();
		try{
			return session.createQuery("from Forbidenword").list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<ForbidenWord> listSome(int start, int limit) {
		Session session=getSession();
		try{
			return session.createQuery("from Forbidenword").setFirstResult(start)
					.setMaxResults(limit).list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public ForbidenWord getById(int id) {
		Session session=getSession();
		try{
			return (ForbidenWord)session.createQuery("from Forbidenword f where f.id:=id")
					.setParameter("id", id);
		}finally{
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
		return 0;
		//to do somethings
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public int add(ForbidenWord bean) {
		Session session=getSession();
		Transaction transaction=session.beginTransaction();
		try{
		    session.save(bean);
		    transaction.commit();
		    return 1;
		}catch(Exception e){
			logger.error("error occurred while persisting {}, {}", bean, e);
			transaction.rollback();
			return 0;
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public int update(ForbidenWord bean) {
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
