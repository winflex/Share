package cc.lixiaohui.share.dao.bean;

import java.util.List;

/**
 * 用户DAO
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午6:28:42
 */
public interface UserDao {
	
	/**
	 * 新增用户
	 * 
	 * @param user 新用户
	 * @return 主键
	 */
	int add(User user);
	
	/**
	 * 删除用户
	 * 
	 * @param id 用户id
	 * @return 影响行数
	 */
	int delete(int id);
	
	
	/**
	 * 获取用户
	 * 
	 * @param start 起始记录
	 * @param limit 长度
	 * @return
	 */
	List<User> list(int start, int limit);
	
	
}
