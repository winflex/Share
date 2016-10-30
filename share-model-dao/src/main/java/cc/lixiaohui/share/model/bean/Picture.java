package cc.lixiaohui.share.model.bean;

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
	
	private Timestamp uploadTime;
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
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
		return String.format("Picture[id=%d, path=%s, uploadTime=%s]", id, path, uploadTime == null ? "null" : uploadTime.toString());
	}
}
