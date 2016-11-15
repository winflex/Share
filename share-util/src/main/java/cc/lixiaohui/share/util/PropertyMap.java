package cc.lixiaohui.share.util;

import java.util.HashMap;

/**
 * @author lixiaohui
 * @date 2016年11月6日 下午6:43:52
 */
public class PropertyMap extends HashMap<String, Object>{

	private static final long serialVersionUID = -5896043241820712310L;
	
	public void setProperty(String key, String value) {
		put(key, value);
	}
	
	public String getStringProperty(String key) {
		Object value = get(key);
		return value == null ? null : String.valueOf(value);
	}
	
	public byte getByteProperty(String key) {
		return Byte.valueOf(getStringProperty(key));
	}
	
	public boolean getBooleanProperty(String key) {
		return Boolean.valueOf(getStringProperty(key));
	}
	
	public Short getShortProperty(String key) {
		return Short.valueOf(getStringProperty(key));
	}
	
	public int getIntProperty(String key) {
		return Integer.valueOf(getStringProperty(key));
	}
	
	public long getLongProperty(String key) {
		return Long.valueOf(getStringProperty(key));
	}
	
	public float getFloatProperty(String key) {
		return Float.valueOf(getStringProperty(key));
	}
	
	public double getDoubleProperty(String key) {
		return Double.valueOf(getStringProperty(key));
	}
	
}
