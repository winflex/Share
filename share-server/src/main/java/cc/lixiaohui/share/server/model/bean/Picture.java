package cc.lixiaohui.share.server.model.bean;

import java.sql.Timestamp;

/**
 * 
 * 图片实体
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:33:15
 * 
 */
public class Picture {
	
	private int id;
	
	private String path;
	
	private String suffix;
	
	private int uploadUserId;
	
	private Timestamp uploadTime;
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	public int getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(int uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
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
	 * @return the uploadTime
	 */
	public Timestamp getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Picture[id=%d, path=%s, suffix=%s, uploadTime=%s]", id, path, suffix, uploadTime == null ? "null" : uploadTime.toString());
	}
}
