package cc.lixiaohui.share.server.model.bean;

/**
 * @author lixiaohui
 * @date 2016年11月2日 下午10:55:58
 */
public class ForbidenWord {
	
	private int id;
	
	private String content;
	
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append("ForbidenWord[")
				.append("id").append("=").append(id)
				.append(",content").append("=").append(content)
				.append("]").toString();
	}
}
