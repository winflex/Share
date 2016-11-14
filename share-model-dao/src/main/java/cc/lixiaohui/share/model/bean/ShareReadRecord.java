package cc.lixiaohui.share.model.bean;

import java.sql.Timestamp;

/**
 * @author lixiaohui
 * @date 2016年11月13日 下午9:52:13
 */
public class ShareReadRecord {
	
	private int id;
	
	private Share share;
	
	private User user;
	
	private Timestamp readTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getReadTime() {
		return readTime;
	}

	public void setReadTime(Timestamp readTime) {
		this.readTime = readTime;
	}
	
}
