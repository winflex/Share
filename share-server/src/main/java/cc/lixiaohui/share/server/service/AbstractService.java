package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.model.dao.BaseDao;
import cc.lixiaohui.share.server.model.util.DaoFactory;

/**
 * Service 通用操作实现
 * @author lixiaohui
 * @date 2016年11月11日 下午9:18:24
 */
public abstract class AbstractService {
	
	/**
	 * 所有服务类的构造参数
	 */
	public static final Class<?>[] CONSTRUCTOR_TYPES = new Class<?>[]{Session.class, Map.class};
	
	protected Map<String, Object> parameters = new HashMap<String, Object>();
	
	protected Session session;
	
	protected DaoFactory daofactory;
	
	protected static final int DEFAULT_START = 0;
	protected static final int DEFAULT_LIMIT = 20;
	
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractService.class);
	
	public AbstractService(Session session, Map<String, Object> parameters) {
		if (parameters != null && parameters.size() > 0) {
			this.parameters = parameters ;
		}
		this.session = session; 
		daofactory = DaoFactory.newInstance();
	}
	
	protected <T> T getDao(Class<? extends BaseDao<?>> daoClass) {
		try {
			return daofactory.getDao(daoClass);
		} catch (Exception e) {
			throw new RuntimeException("no such dao class: " + daoClass.getSimpleName());
		}
	}
	
	// ===============================
	
	/**
	 * 没找到抛异常
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getObjectParameter(String name) {
		Object o = parameters.get(name);
		if (o != null) {
			return (T) o;
		}
		throw new IllegalArgumentException("parameter " + name + " not found");
	}
	
	/**
	 * 没找到返回defValue
	 * @param name
	 * @param defValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getObjectParameter(String name, T defValue) {
		Object o = parameters.get(name);
		return o == null ? defValue : (T)o;
	}
	
	protected String getStringParameter(String name) {
		Object o = parameters.get(name);
		if (o != null) {
			return String.valueOf(o);
		}
		throw new IllegalArgumentException("parameter " + name + " not found");
	}
	
	protected String getStringParameter(String name, String defValue) {
		Object o = getObjectParameter(name, null);
		return o == null ? defValue : String.valueOf(o);
	}
	
	protected byte getByteParameter(String name) {
		return Byte.valueOf(getStringParameter(name));
	}
	
	
	protected byte getByteParameter(String name, byte defValue) {
		String s = getStringParameter(name, null);
		return s == null ? defValue : Byte.valueOf(s);
	}
	
	protected short getShortParameter(String name) {
		return Short.valueOf(getStringParameter(name));
	}
	
	protected short getShortParameter(String name, short defValue) {
		String s = getStringParameter(name, null);
		return s == null ? defValue : Short.valueOf(s);
	}
	
	protected int getIntParameter(String name) {
		return Integer.valueOf(getStringParameter(name));
	}
	
	protected int getIntParameter(String name, int defValue) {
		String s = getStringParameter(name, null);
		return s == null ? defValue : Integer.valueOf(s);
	}
	
	protected long getLongParameter(String name) {
		return Long.valueOf(getStringParameter(name));
	}
	
	protected long getLongParameter(String name, long defValue) {
		String s = getStringParameter(name, null);
		return s == null ? defValue : Long.valueOf(s);
	}
	
	protected float getFloatParameter(String name) {
		return Float.valueOf(getStringParameter(name));
	}
	
	protected float getFloatParameter(String name, float defValue) {
		String s = getStringParameter(name, null);
		return s == null ? defValue : Float.valueOf(s);
	}
	
	protected double getDoubleParameter(String name) {
		return Double.valueOf(getStringParameter(name));
	}
	
	protected double getDoubleParameter(String name, double defValue) {
		String s = getStringParameter(name, null);
		return s == null ? defValue : Double.valueOf(s);
	}
	
	protected boolean getBooleanParameter(String name) {
		return Boolean.valueOf(getStringParameter(name));
	}
	
	protected boolean getBooleanParameter(String name, boolean defValue) {
		String s = getStringParameter(name, null);
		return s == null ? defValue : Boolean.valueOf(s);
	}
	
}
