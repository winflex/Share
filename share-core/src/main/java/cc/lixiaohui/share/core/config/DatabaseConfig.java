package cc.lixiaohui.share.core.config;

/**
 * 数据库相关配置
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午11:54:13
 */
public class DatabaseConfig {
	
	private int defaultPageSize = 20;
	
	private MyBatisConfig myBatisConfig;

	/**
	 * @return the defaultPageSize
	 */
	public int getDefaultPageSize() {
		return defaultPageSize;
	}

	/**
	 * @param defaultPageSize the defaultPageSize to set
	 */
	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

	/**
	 * @return the myBatisConfig
	 */
	public MyBatisConfig getMyBatisConfig() {
		return myBatisConfig;
	}

	/**
	 * @param myBatisConfig the myBatisConfig to set
	 */
	public void setMyBatisConfig(MyBatisConfig myBatisConfig) {
		this.myBatisConfig = myBatisConfig;
	}
	
}
