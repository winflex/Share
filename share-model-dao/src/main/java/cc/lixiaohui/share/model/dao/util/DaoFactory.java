package cc.lixiaohui.share.model.dao.util;

import java.util.HashMap;
import java.util.Map;

import cc.lixiaohui.share.model.dao.CommentDao;
import cc.lixiaohui.share.model.dao.ForbidenWordDao;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.dao.PraiseDao;
import cc.lixiaohui.share.model.dao.RoleDao;
import cc.lixiaohui.share.model.dao.ShareCollectionDao;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.model.dao.UserCollectionDao;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.model.dao.impl.AbstractBaseDao;
import cc.lixiaohui.share.model.dao.impl.CommentDaoImpl;
import cc.lixiaohui.share.model.dao.impl.ForbidenWordDaoImpl;
import cc.lixiaohui.share.model.dao.impl.PictureDaoImpl;
import cc.lixiaohui.share.model.dao.impl.PraiseDaoImpl;
import cc.lixiaohui.share.model.dao.impl.RoleDaoImpl;
import cc.lixiaohui.share.model.dao.impl.ShareCollectionDaoImpl;
import cc.lixiaohui.share.model.dao.impl.ShareDaoImpl;
import cc.lixiaohui.share.model.dao.impl.UserCollectionDaoImpl;
import cc.lixiaohui.share.model.dao.impl.UserDaoImpl;

/**
 * 维护Dao映射, 外置实现覆盖内置实现
 * 
 * @author lixiaohui
 * @date 2016年10月30日 下午8:58:09
 */
public class DaoFactory {
	
	//UserDao.class -> UserDaoImpl
	private Map<Class<?>, AbstractBaseDao<?>> daoMap = new HashMap<Class<?>, AbstractBaseDao<?>>();
	
	public DaoFactory() {
		this(null);
	}
	
	/**
	 * 外部Dao实现
	 * @param map
	 */
	public DaoFactory(Map<Class<?>, AbstractBaseDao<?>> map) {
		putInternal();
		if (map != null && map.size() > 0) {
			daoMap.putAll(map);
		}
	}
	// 加入内置dao实现
	private void putInternal() {
		daoMap.put(UserDao.class, new UserDaoImpl());
		daoMap.put(CommentDao.class, new CommentDaoImpl());
		daoMap.put(ForbidenWordDao.class, new ForbidenWordDaoImpl());
		daoMap.put(PictureDao.class, new PictureDaoImpl());
		daoMap.put(PraiseDao.class, new PraiseDaoImpl());
		daoMap.put(RoleDao.class, new RoleDaoImpl());
		daoMap.put(ShareDao.class, new ShareDaoImpl());
		daoMap.put(UserCollectionDao.class, new UserCollectionDaoImpl());
		daoMap.put(ShareCollectionDao.class, new ShareCollectionDaoImpl());
	}

	@SuppressWarnings("unchecked")
	public <T> T getDao(Class<T> clazz) {
		return (T) daoMap.get(clazz);
	}
	
}
