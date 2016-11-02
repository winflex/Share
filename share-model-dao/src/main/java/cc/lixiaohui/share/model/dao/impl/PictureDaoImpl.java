package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Picture;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:20:33
 */
public class PictureDaoImpl extends SimpleDaoSupport implements PictureDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);
	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<Picture> list() {
	    Session session=getSession();
	    try{
	    	return session.createQuery("from Picture").list();
	    }finally{
	    	session.close();
	    }
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<Picture> listSome(int start, int limit) {
		Session session=getSession();
		try{
			return session.createQuery("from Picture").setFirstResult(start)
					.setMaxResults(limit).list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public Picture getById(int id) {
		Session session=getSession();
		try{
			return (Picture)session.createQuery("from Picture p where p := id")
					.setParameter("id", id).uniqueResult();
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
			Picture picture=new Picture();
			picture.setId(id);
			session.delete(picture);
			transaction.commit();
			return 1;
		}catch(Exception e){
			logger.error("error occurred while deleting picture[id = {}], {}", id, e);
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
	public int add(Picture bean) {
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
	public int update(Picture bean) {
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
