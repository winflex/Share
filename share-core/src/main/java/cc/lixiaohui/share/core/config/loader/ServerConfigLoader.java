package cc.lixiaohui.share.core.config.loader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.RuleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import cc.lixiaohui.share.core.config.ConfigException;
import cc.lixiaohui.share.core.config.ServerConfig;
import cc.lixiaohui.share.core.config.loader.rule.ServerConfigRuleSet;
import cc.lixiaohui.share.util.FileUtils;

/**
 * 配置文件加载类
 * 
 * @author lixiaohui
 * @date 2016年10月30日 上午12:07:07
 */
public class ServerConfigLoader {
	
	private Logger logger = LoggerFactory.getLogger(ServerConfigLoader.class);
	
	// 规则集缓存
	private RuleSet ruleSet;
	
	// 配置文件路径
	private String configFilePath;
	
	private Object ruleSetLock = new Object();
	
	public ServerConfigLoader(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	/**
	 * 加载配置文件
	 * @return 配置bean
	 * @throws ConfigException 配置文件不存在或者配置文件格式错误
	 */
	public ServerConfig load() throws ConfigException {
		try {
			return load(FileUtils.getResourceAsStream(configFilePath));
		} catch (IOException e) {
			logger.error("config file not found {}", configFilePath);
			throw new ConfigException(e);
		}
	}
	
	protected ServerConfig load(InputStream stream) throws ConfigException{
		Digester digester = new Digester();
		digester.addRuleSet(createRuleSet());
		try {
			ServerConfig config = digester.parse(stream);
			return config;
		} catch (IOException e) {
			logger.error("error occurred while parsing server.xml, please check if the configuration "
					+ "path({}) giving is right", configFilePath);
			throw new ConfigException(e);
		} catch (SAXException e) {
			logger.error("error occurred while parsing server.xml, please check the syntax of the "
					+ "configuration file({})", configFilePath);
			throw new ConfigException(e);
		}
	}
	
	private RuleSet createRuleSet() {
		if (ruleSet == null) {
			synchronized (ruleSetLock) {
				if (ruleSet == null) {
					ruleSet = new ServerConfigRuleSet();
				}
			}
		}
		return ruleSet;
	}
	
}
