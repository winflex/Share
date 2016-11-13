package cc.lixiaohui.share.model.bean;

import java.sql.Timestamp;

/**
 * 好友关系
 * 
 * @author lixiaohui
 * @date 2016年11月2日 下午11:10:35
 */
public class FriendShip {
	
	private int id;
	
	private User askUser;
	
	private User askedUser;
	
	private boolean pending;
	
	private Timestamp askTime;
	
	private Timestamp answerTime;

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


	public User getAskUser() {
		return askUser;
	}

	public void setAskUser(User askUser) {
		this.askUser = askUser;
	}

	public User getAskedUser() {
		return askedUser;
	}

	public void setAskedUser(User askedUser) {
		this.askedUser = askedUser;
	}

	/**
	 * @return the pending
	 */
	public boolean isPending() {
		return pending;
	}

	/**
	 * @param pending the pending to set
	 */
	public void setPending(boolean pending) {
		this.pending = pending;
	}

	/**
	 * @return the askTime
	 */
	public Timestamp getAskTime() {
		return askTime;
	}

	/**
	 * @param askTime the askTime to set
	 */
	public void setAskTime(Timestamp askTime) {
		this.askTime = askTime;
	}

	/**
	 * @return the answerTime
	 */
	public Timestamp getAnswerTime() {
		return answerTime;
	}

	/**
	 * @param answerTime the answerTime to set
	 */
	public void setAnswerTime(Timestamp answerTime) {
		this.answerTime = answerTime;
	}
	
}
