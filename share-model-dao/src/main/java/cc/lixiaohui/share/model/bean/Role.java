package cc.lixiaohui.share.model.bean;

import java.sql.Timestamp;

/**
 * 固定三个等级:super, admin, normal
 * @author lixiaohui
 * @date 2016年10月29日 下午5:39:43
 */
public class Role {
	
	public static final int SUPER = 0;
	public static final int ADMIN = 1;
	public static final int NORMAL = 2;
	
	public static boolean isSuper(int roleId) {
		return roleId == SUPER;
	}
	
	public static boolean isAdmin(int userId) {
		return userId == SUPER || userId == ADMIN;
	}
	
	private int id;
	
	private String description;
	
	private Timestamp createTime;
	
	private boolean deleted;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String desc = description == null ? "null" : description;
		String createTime = this.createTime == null ? "null" : this.createTime.toString();
		return String.format("Role[id=%d, desc=%s, createTime=%s, deleted=%s]", id, desc, createTime, String.valueOf(deleted));
	}
}
