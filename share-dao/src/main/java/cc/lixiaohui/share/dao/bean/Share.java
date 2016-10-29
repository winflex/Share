package cc.lixiaohui.share.dao.bean;

import java.sql.Timestamp;
import java.util.List;

/**
 * 分享内容实体
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:04:25
 * 
 */
public class Share {
	
	private int id;
	
	private User publisher;
	
	private String content;
	
	private List<Picture> pictures;
	
	private Timestamp createTime;
	
	private int praiseCount;
	
	private List<Praise> praises;
	
	private boolean deleted;

	private List<Comment> comments;
	
	
	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
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
	 * @return the publisher
	 */
	public User getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(User publisher) {
		this.publisher = publisher;
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
	 * @return the pictures
	 */
	public List<Picture> getPictures() {
		return pictures;
	}

	/**
	 * @param pictures the pictures to set
	 */
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
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
	 * @return the praiseCount
	 */
	public int getPraiseCount() {
		return praiseCount;
	}

	/**
	 * @param praiseCount the praiseCount to set
	 */
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	/**
	 * @return the praises
	 */
	public List<Praise> getPraises() {
		return praises;
	}

	/**
	 * @param praises the praises to set
	 */
	public void setPraises(List<Praise> praises) {
		this.praises = praises;
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
