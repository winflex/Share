package cc.lixiaohui.share.model.bean;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户实体
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午4:51:09
 * 
 */

public class User {
	
	private int id;
	
	private String username;
	
	private String password;
	
	private String sex;
	
	private String signature;
	
	private Role role;
	
	private Timestamp registerTime;
	
	private Picture headImage;
	
	private int headImageId;//为update时用
	
	private boolean selfForbid;
	
	private boolean adminForbid;
	
	private boolean deleted;
	
	/**
	 * 自己发布的分享
	 */
	private List<Share> shares;
	
	/**
	 * 收藏的分享
	 */
	private List<ShareCollection> shareCollections;
	
	/**
	 * 收藏的用户
	 */
	private List<UserCollection> userCollections;
	
	/**
	 * 好友
	 */
	private List<User> friends;
	 
	/**
	 * 我对别人的评论
	 */
	private List<Comment> commentsByMe;
	
	/**
	 * 别人对自己的评论
	 */
	private List<Comment> commentsForMe;
	
	
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
	 * @return the commentsByMe
	 */
	public List<Comment> getCommentsByMe() {
		return commentsByMe;
	}

	/**
	 * @param commentsByMe the commentsByMe to set
	 */
	public void setCommentsByMe(List<Comment> commentsByMe) {
		this.commentsByMe = commentsByMe;
	}

	/**
	 * @return the commentsForMe
	 */
	public List<Comment> getCommentsForMe() {
		return commentsForMe;
	}

	/**
	 * @param commentsForMe the commentsForMe to set
	 */
	public void setCommentsForMe(List<Comment> commentsForMe) {
		this.commentsForMe = commentsForMe;
	}

	/**
	 * @return the headImageId
	 */
	public int getHeadImageId() {
		return headImageId;
	}

	/**
	 * @param headImageId the headImageId to set
	 */
	public void setHeadImageId(int headImageId) {
		this.headImageId = headImageId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}



	/**
	 * @return the registerTime
	 */
	public Timestamp getRegisterTime() {
		return registerTime;
	}

	/**
	 * @param registerTime the registerTime to set
	 */
	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	/**
	 * @return the selfForbid
	 */
	public boolean isSelfForbid() {
		return selfForbid;
	}

	/**
	 * @param selfForbid the selfForbid to set
	 */
	public void setSelfForbid(boolean selfForbid) {
		this.selfForbid = selfForbid;
	}

	/**
	 * @return the adminForbid
	 */
	public boolean isAdminForbid() {
		return adminForbid;
	}

	/**
	 * @param adminForbid the adminForbid to set
	 */
	public void setAdminForbid(boolean adminForbid) {
		this.adminForbid = adminForbid;
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

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the headImage
	 */
	public Picture getHeadImage() {
		return headImage;
	}

	/**
	 * @param headImage the headImage to set
	 */
	public void setHeadImage(Picture headImage) {
		this.headImage = headImage;
	}

	/**
	 * @return the shareCollections
	 */
	public List<ShareCollection> getShareCollections() {
		return shareCollections;
	}

	/**
	 * @param shareCollections the shareCollections to set
	 */
	public void setShareCollections(List<ShareCollection> shareCollections) {
		this.shareCollections = shareCollections;
	}

	/**
	 * @return the userCollections
	 */
	public List<UserCollection> getUserCollections() {
		return userCollections;
	}

	/**
	 * @param userCollections the userCollections to set
	 */
	public void setUserCollections(List<UserCollection> userCollections) {
		this.userCollections = userCollections;
	}

	/**
	 * @return the friends
	 */
	public List<User> getFriends() {
		return friends;
	}

	/**
	 * @param friends the friends to set
	 */
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	
	/**
	 * 为解决mybatis插入时不能通过引用链取值
	 * @return
	 */
	public int getRoleId() {
		return role.getId();
	}
	
	/**
	 * @return the shares
	 */
	public List<Share> getShares() {
		return shares;
	}

	/**
	 * @param shares the shares to set
	 */
	public void setShares(List<Share> shares) {
		this.shares = shares;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder("User[id=").append(id)
				.append(", name=").append(username)
				.append(", password=").append(password)
				.append(", sex=").append(sex)
				.append(", signature=").append(signature)
				.append(", role=").append(role == null ? "null" : role.toString())
				.append(", headImage=").append(headImage == null ? "null" : headImage.toString())
				.append(", selfFirbid=").append(selfForbid)
				.append(", adminForbid=").append(adminForbid)
				.append(", deleted=").append(deleted).append("]").toString();
	}
}
