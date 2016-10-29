package cc.lixiaohui.share.dao.bean;

/**
 * 
 * 图片实体
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午5:33:15
 * 
 */
public class Picture {
	
	private String path;
	
	private boolean usable;

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
