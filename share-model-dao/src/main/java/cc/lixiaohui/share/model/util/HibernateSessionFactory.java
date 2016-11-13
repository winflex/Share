package cc.lixiaohui.share.model.util;

import java.io.File;

import org.hibernate.HibernateException;
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
	
	private static SessionFactory factory;

	private static String hibernateConfigFilePath;
	
	public static SessionFactory getSessionFactory() {
		if (factory == null) {
			synchronized (HibernateSessionFactory.class){
				if (factory == null) {
					newSessionFactory();
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

	private static void newSessionFactory() {
		try {
			// 优先从classpath中加载
			factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			// classpath下没有从指定的文件加载
			if (factory == null) {
				File configFile = new File(hibernateConfigFilePath);
				factory = new Configuration().configure(configFile).buildSessionFactory();
			}
		} catch (HibernateException e) {
			logger.error("{}", e);
		}
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
