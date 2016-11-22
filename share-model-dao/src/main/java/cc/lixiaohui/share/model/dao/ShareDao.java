package cc.lixiaohui.share.model.dao;

import java.util.List;

import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.dao.util.DaoException;

/**
 * @author lixiaohui
 * @date 2016年10月30日 下午5:00:06
 */
public interface ShareDao extends DeleteableDao<Share> {
	
	public Share getByIdForDetail(int shareId);
	
	public Share getByIdForComment(int shareId);
	
	public List<Share> list(int currentUserId, int targetUserId, String keyword, long baseTime, boolean deleted, String orderColumnName, String orderTypeName, int start, int limit) throws DaoException;
}
