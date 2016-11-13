package cc.lixiaohui.share.model.util;

import java.util.HashMap;
import java.util.Map;

import cc.lixiaohui.share.model.dao.BaseDao;
import cc.lixiaohui.share.model.dao.CommentDao;
import cc.lixiaohui.share.model.dao.ForbidenWordDao;
import cc.lixiaohui.share.model.dao.FriendShipDao;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.dao.PraiseDao;
import cc.lixiaohui.share.model.dao.RoleDao;
import cc.lixiaohui.share.model.dao.ShareCollectionDao;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.model.dao.SortingDao;
import cc.lixiaohui.share.model.dao.UserCollectionDao;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.model.dao.impl.CommentDaoImpl;
import cc.lixiaohui.share.model.dao.impl.ForbidenWordDaoImpl;
import cc.lixiaohui.share.model.dao.impl.FriendShipDaoImpl;
import cc.lixiaohui.share.model.dao.impl.PictureDaoImpl;
import cc.lixiaohui.share.model.dao.impl.PraiseDaoImpl;
import cc.lixiaohui.share.model.dao.impl.RoleDaoImpl;
import cc.lixiaohui.share.model.dao.impl.ShareCollectionDaoImpl;
import cc.lixiaohui.share.model.dao.impl.ShareDaoImpl;
import cc.lixiaohui.share.model.dao.impl.SortingDaoImpl;
import cc.lixiaohui.share.model.dao.impl.UserCollectionDaoImpl;
import cc.lixiaohui.share.model.dao.impl.UserDaoImpl;

/**
 * 维护Dao映射, 若提供外置dao实现, 则外置实现会覆盖内置实现
 * 
 * @author lixiaohui
 * @date 2016年10月30日 下午8:58:09
 */
public class DaoFactory {
	
	//UserDao.class -> UserDaoImpl.class
	private static Map<Class<? extends BaseDao<?>>, Class<? extends BaseDao<?>>> daoMap = new HashMap<Class<? extends BaseDao<?>>, Class<? extends BaseDao<?>>>();
	
	static {
		daoMap.put(UserDao.class, UserDaoImpl.class);
		daoMap.put(CommentDao.class, CommentDaoImpl.class);
		daoMap.put(ForbidenWordDao.class, ForbidenWordDaoImpl.class);
		daoMap.put(PictureDao.class, PictureDaoImpl.class);
		daoMap.put(PraiseDao.class, PraiseDaoImpl.class);
		daoMap.put(RoleDao.class, RoleDaoImpl.class);
		daoMap.put(ShareDao.class, ShareDaoImpl.class);
		daoMap.put(UserCollectionDao.class, UserCollectionDaoImpl.class);
		daoMap.put(ShareCollectionDao.class, ShareCollectionDaoImpl.class);
		daoMap.put(SortingDao.class, SortingDaoImpl.class);
		daoMap.put(FriendShipDao.class, FriendShipDaoImpl.class);
	}
	
	public DaoFactory() {
		this(null);
	}
	
	/**
	 * 外部Dao实现
	 * @param map
	 */
	public DaoFactory(Map<Class<BaseDao<?>>, Class<BaseDao<?>>> map) {
		if (map != null && map.size() > 0) {
			daoMap.putAll(map);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getDao(Class<? extends BaseDao<?>> clazz) throws InstantiationException, IllegalAccessException {
		Class<? extends BaseDao<?>> foundClass = daoMap.get(clazz);
		return (T) foundClass.newInstance();
	}

	
	public static DaoFactory newInstance() {
		return new DaoFactory();
	}
}
