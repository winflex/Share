package cc.lixiaohui.share.util;

import java.util.HashMap;

/**
 * @author lixiaohui
 * @date 2016年11月6日 下午6:43:52
 */
public class PropertyMap extends HashMap<String, String>{

	private static final long serialVersionUID = -5896043241820712310L;
	
	public void setProperty(String key, String value) {
		put(key, value);
	}
	
	public String getProperty(String key) {
		return get(key);
	}
	
	public void setByteProperty(String key, byte value) {
		put(key, String.valueOf(value));
	}
	
	public byte getByteProperty(String key) {
		return Byte.valueOf(get(key));
	}
	
	public void setBooleanProperty(String key, boolean value) {
		put(key, String.valueOf(value));
	}
	
	public boolean getBooleanProperty(String key) {
		return Boolean.valueOf(get(key));
	}
	
	public void setShortProperty(String key, short value) {
		put(key, String.valueOf(value));
	}
	
	public Short getShortProperty(String key) {
		return Short.valueOf(get(key));
	}
	
	public void setIntProperty(String key, int value) {
		put(key, String.valueOf(value));
	}
	
	public int getIntProperty(String key) {
		return Integer.valueOf(get(key));
	}
	
	public void setLongProperty(String key, long value) {
		put(key, String.valueOf(value));
	}
	
	public long getLongProperty(String key) {
		return Long.valueOf(get(key));
	}
	
	public void setFloatProperty(String key, float value) {
		put(key, String.valueOf(value));
	}
	
	public float getFloatProperty(String key) {
		return Float.valueOf(get(key));
	}
	
	public void setDoubleProperty(String key, double value) {
		put(key, String.valueOf(value));
	}
	
	public double getDoubleProperty(String key) {
		return Double.valueOf(get(key));
	}
	
}
