package cc.lixiaohui.share.dao.bean;

/**
 * 过滤词实体
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:06:18
 * 
 */
public class ForbidenWord {
	
	private String content;
	
	private boolean usable;

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
	 * @return the usable
	 */
	public boolean isUsable() {
		return usable;
	}

	/**
	 * @param usable the usable to set
	 */
	public void setUsable(boolean usable) {
		this.usable = usable;
	}

}
