package cc.lixiaohui.share.server.config;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaohui.share.server.config.util.Property;

/**
 * 数据库相关配置
 * 
 * @author lixiaohui
 * @date 2016年10月29日 下午11:54:13
 */
public class DatabaseConfig {
	
	private int defaultPageSize = 20;
	
	private String url;
	
	private String driverClass;
	
	private String username;
	
	private String password;
	
	private List<Property> properties = new ArrayList<Property>();
	
	public void addProperty(Property property) {
		properties.add(property);
	}
	
	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public int getDefaultPageSize() {
		return defaultPageSize;
	}

	public String getUrl() {
		return url;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

}
