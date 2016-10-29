package cc.lixiaohui.share.dao.bean;

import java.sql.Timestamp;

/**
 * 用户收藏的用户
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:53:01
 */
public class UserCollection {
	
	private int id;
	
	private User user;
	
	private Timestamp collectTime;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the collectTime
	 */
	public Timestamp getCollectTime() {
		return collectTime;
	}

	/**
	 * @param collectTime the collectTime to set
	 */
	public void setCollectTime(Timestamp collectTime) {
		this.collectTime = collectTime;
	}
	
}
