package cc.lixiaohui.share.model.bean;

import java.sql.Timestamp;

/**
 * 评论实体
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:02:19
 * 
 */
public class Comment {
	
	private int id;
	
	private Share share;
	
	private String content;
	
	private User fromUser;
	
	private User toUser;
	
	private short kind;
	
	private Timestamp commentTime;
	
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the fromUser
	 */
	public User getFromUser() {
		return fromUser;
	}

	/**
	 * @param fromUser the fromUser to set
	 */
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * @return the toUser
	 */
	public User getToUser() {
		return toUser;
	}

	/**
	 * @param toUser the toUser to set
	 */
	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	/**
	 * @return the kind
	 */
	public short getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind(short kind) {
		this.kind = kind;
	}

	/**
	 * @return the commentTime
	 */
	public Timestamp getCommentTime() {
		return commentTime;
	}

	/**
	 * @param commentTime the commentTime to set
	 */
	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
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

}
