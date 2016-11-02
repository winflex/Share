package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:30:28
 */
public class ShareDaoImpl extends SimpleDaoSupport implements ShareDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Share> list() {
		Session session = getSession();
		try {
			return session.createQuery("from Share").list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Share> listSome(int start, int limit) {
		Session session = getSession();
		try{
			return getSession().createQuery("from Share").setFirstResult(start).
					setMaxResults(limit).list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Share getById(int id) {
		Session session=getSession();
		try{
			return (Share)getSession().createQuery("from Share s where s.id := shareId").
					setParameter("shareId", id);
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
		try{
			Share share=new Share();
			share.setId(id);
			session.delete(share);
			transaction.commit();
			return 1;
		}catch(Exception e){
			logger.error("error occurred while delating share[id = {}],{}",id,e);
			transaction.rollback();
			return 0;
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public int add(Share bean) {
		Session session=getSession();
		Transaction transaction=session.beginTransaction();
		try{
			session.save(bean);
			transaction.commit();
			return 1;
		}catch(Exception e){
			logger.error("error occured while persisting{},{}",bean,e);
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
	public int update(Share bean) {
		Session session=getSession();
		Transaction transaction=session.beginTransaction();
		try{
			session.update(bean);
		    transaction.commit();
		    return 1;
		}catch(Exception e){
			logger.error("error occured while persisting{},{}",bean,e);
			transaction.rollback();
			return 0;
		}finally{
			session.close();
		}
	}

}
