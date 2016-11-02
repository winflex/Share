package cc.lixiaohui.share.model.dao.impl;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.bean.ShareCollection;
import cc.lixiaohui.share.model.dao.ShareCollectionDao;
import cc.lixiaohui.share.model.dao.SimpleDaoSupport;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午9:21:44
 */
public class ShareCollectionDaoImpl extends SimpleDaoSupport implements ShareCollectionDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#list()
	 */
	@Override
	public List<ShareCollection> list() {
		Session session = getSession();
		try {
			return session.createQuery("from share_collection").list();
		} finally {
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#listSome(int, int)
	 */
	@Override
	public List<ShareCollection> listSome(int start, int limit) {
		Session session = getSession();
		try{
			return getSession().createQuery("from share_collection").setFirstResult(start).
					setMaxResults(limit).list();
		}finally{
			session.close();
		}
	}

	/* 
	 * @see cc.lixiaohui.share.model.dao.BaseDao#getById(int)
	 */
	@Override
	public ShareCollection getById(int id) {
		Session session=getSession();
		try{
			return (ShareCollection)getSession().createQuery("from share_collection s where s.id := share"
					+ "CollectionId").
					setParameter("shareCollectionId", id);
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
			ShareCollection shareCollection=new ShareCollection();
			shareCollection.setId(id);
			session.delete(shareCollection);
			transaction.commit();
			return 1;
		}catch(Exception e){
			logger.error("error occurred while delating ShareCollection[id = {}],{}",id,e);
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
	public int add(ShareCollection bean) {
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
	public int update(ShareCollection bean) {
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
