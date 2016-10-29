package cc.lixiaohui.share.dao.bean;

import java.sql.Timestamp;

/**
 * 用户收藏的分享
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:51:26
 */
public class ShareCollection {
	
	private int id;
	
	private Share share;
	
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
