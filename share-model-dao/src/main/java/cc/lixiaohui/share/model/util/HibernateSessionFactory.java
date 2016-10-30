package cc.lixiaohui.share.model.util;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HibernateSessionFactory工程
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
				newSessionFactory();
			}
		}
		return factory;
	}
	
	/**
	 * 提供外部配置文件
	 * @param path
	 */
	public static void offerPath(String filepath) {
		if (factory != null) {
			hibernateConfigFilePath = filepath;
		}
	}

	private static void newSessionFactory() {
		
		try {
			if (hibernateConfigFilePath == null) {
				factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			} else {
				File configFile = new File(hibernateConfigFilePath);
				factory = new Configuration().configure(configFile).buildSessionFactory();
			}
		} catch (HibernateException e) {
			logger.error("{}", e);
		}
	}

	
	public static void main(String[] args) {
		getSessionFactory();
	}
}
