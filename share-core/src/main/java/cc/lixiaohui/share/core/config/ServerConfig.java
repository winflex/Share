package cc.lixiaohui.share.core.config;

/**
 * 服务器配置
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午11:51:33
 */
public class ServerConfig {
	
	private String name;
	
	private SocketConfig socketConfig;
	
	private DatabaseConfig databaseConfig;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the socketConfig
	 */
	public SocketConfig getSocketConfig() {
		return socketConfig;
	}

	/**
	 * @param socketConfig the socketConfig to set
	 */
	public void setSocketConfig(SocketConfig socketConfig) {
		this.socketConfig = socketConfig;
	}

	/**
	 * @return the databaseConfig
	 */
	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	/**
	 * @param databaseConfig the databaseConfig to set
	 */
	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}
	
}
