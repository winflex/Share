package cc.lixiaohui.share.server.model.util;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.server.core.config.DatabaseConfig;
import cc.lixiaohui.share.server.core.config.util.Property;

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
			for (Property property : config.getProperties()) {
				conf.setProperty(property.getName(), property.getValue());
			}
		}
		factory = conf.buildSessionFactory();
		factory.openSession();
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
