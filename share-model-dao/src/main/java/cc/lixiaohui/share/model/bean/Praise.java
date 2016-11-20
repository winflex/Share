package cc.lixiaohui.share.model.bean;

import java.sql.Timestamp;

/**
 * 赞实体
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:34:12
 */
public class Praise {
	
	private int id;
	
	private User user;
	
	private Share share;
	
	private Timestamp praiseTime;

	public Praise(){}
	
	public Praise(User user, Share share){
		this.user = user;
		this.share = share;
	}
	
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
	 * @return the share
	 */
	public Share getShare() {
		return share;
	}

	/**
	 * @param share the share to set
	 */
	public void setShare(Share share) {
		this.share = share;
	}

	/**
	 * @return the praiseTime
	 */
	public Timestamp getPraiseTime() {
		return praiseTime;
	}

	/**
	 * @param praiseTime the praiseTime to set
	 */
	public void setPraiseTime(Timestamp praiseTime) {
		this.praiseTime = praiseTime;
	}

}
