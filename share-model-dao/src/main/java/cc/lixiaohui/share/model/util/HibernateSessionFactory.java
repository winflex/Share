package cc.lixiaohui.share.model.util;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HibernateSessionFactory工厂, 单例, 线程安全
 * 
 * 优先加载classpath下的
 * 
 * @author lixiaohui
 * @date 2016年10月30日 下午4:26:34
 */
public class HibernateSessionFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(HibernateSessionFactory.class);
	
	private static volatile SessionFactory factory;

	private static volatile String hibernateConfigFilePath;
	
	public static void init(DatabaseConfig dbConfig) {
		if (factory == null) {
			synchronized (HibernateSessionFactory.class){
				if (factory == null) {
					newSessionFactory(dbConfig);
				}
			}
		}
	}
	
	public static SessionFactory getSessionFactory() {
		if (factory == null) {
			synchronized (HibernateSessionFactory.class){
				if (factory == null) {
					newSessionFactory(null);
				}
			}
		}
		return factory;
	}
	
	/**
	 * 提供外部配置文件, 当factory实例已生成时无效
	 * @param path
	 */
	public static void offerExternalPath(String filepath) {
		if (factory != null) {
			hibernateConfigFilePath = filepath;
		}
	}

	private static void newSessionFactory(DatabaseConfig config) {
		// 优先从classpath中加载
		Configuration conf = new Configuration().configure("hibernate.cfg.xml");
		// classpath下没有从指定的文件加载
		if (conf == null) {
			File configFile = new File(hibernateConfigFilePath);
			conf = new Configuration().configure(configFile);
			logger.info("using external hibernate.cfg.xml");
		} else {
			logger.info("using internal hibernate.cfg.xml");
		}
		if (config != null) {
			if (config.getUrl() != null && !config.getUrl().equals("")) {
				conf.setProperty("hibernate.connection.url", config.getUrl());
				logger.debug("replacing hibernate.connection.url with {}", config.getUrl());
			}
			if (config.getDriverClass() != null && !config.equals("")) {
				conf.setProperty("hibernate.connection.driver_class", config.getDriverClass());
				logger.debug("replacing hibernate.connection.driver_class with {}", config.getDriverClass());
			}
			if (config.getUsername() != null && !config.getUsername().equals("")) {
				conf.setProperty("hibernate.connection.username", config.getUsername());
				logger.debug("replacing hibernate.connection.username with {}", config.getUsername());
			}
			if (config.getPassword() != null && !config.getPassword().equals("")) {
				conf.setProperty("hibernate.connection.password", config.getPassword());
				logger.debug("replacing hibernate.connection.password with {}", config.getPassword());
			}
		}
		factory = conf.buildSessionFactory();
		//factory.openSession();
	}

	
	/**
	 * @param hibernateConfigFilePath the hibernateConfigFilePath to set
	 */
	public static void setHibernateConfigFilePath(String path) {
		if (hibernateConfigFilePath != null) {
			hibernateConfigFilePath = path;
		}
	}

	public static void main(String[] args) {
		getSessionFactory();
	}
}
