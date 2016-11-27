package cc.lixiaohui.share.server.config;


/**
 * 服务器配置
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午11:51:33
 */
public class ServerConfig {
	
	/** 服务器名字 */
	private String name;
	
	/**业务线程池参数*/
	private PoolConfig poolConfig;
	
	/** Socket参数配置 */
	private SocketConfig socketConfig;
	
	/** 数据库参数配置 */
	private DatabaseConfig databaseConfig;
	
	/** 会话参数配置 */
	private SessionConfig sessionConfig;

	/**
	 * @return the sessionConfig
	 */
	public SessionConfig getSessionConfig() {
		return sessionConfig;
	}

	/**
	 * @param sessionConfig the sessionConfig to set
	 */
	public void setSessionConfig(SessionConfig sessionConfig) {
		this.sessionConfig = sessionConfig;
	}

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
	 * @return the poolConfig
	 */
	public PoolConfig getPoolConfig() {
		return poolConfig;
	}

	/**
	 * @param poolConfig the poolConfig to set
	 */
	public void setPoolConfig(PoolConfig poolConfig) {
		this.poolConfig = poolConfig;
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
