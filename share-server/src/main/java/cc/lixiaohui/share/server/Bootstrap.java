package cc.lixiaohui.share.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.server.config.ConfigException;
import cc.lixiaohui.share.server.config.ServerConfig;
import cc.lixiaohui.share.server.config.loader.ServerConfigLoader;
import cc.lixiaohui.share.util.FileUtils;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

/**
 * 启动类
 * @author lixiaohui
 * @date 2016年11月8日 下午11:33:22
 */
public class Bootstrap {
	
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	public void start(String[] args) throws ConfigException, LifeCycleException {
		String confPath = FileUtils.concatPath(findServerHome(), SystemRuntime.DIR_CONF, SystemRuntime.CONF_NAME);
		ServerConfig config = new ServerConfigLoader(confPath).load();
		ShareServer server = new ShareServer(config);
		try {
			server.start();
		} catch (LifeCycleException e) {
			logger.error("failed to start server, {}", e);
			throw e;
		}
	}
	
	// server home must be absolute path
	private String findServerHome() throws LifeCycleException {
		String home = java.lang.System.getProperty(SystemRuntime.SERVER_HOME);
		if (home == null || home.equals("")) {
			throw new LifeCycleException("server home not found, please reboot using java -Dserver.home=your/server/home");
		}
		return home;
	}
	
	public static void main(String[] args) {
		try {
			new Bootstrap().start(args);
		} catch (Exception e) {
			logger.error("failed to start server, exit right now, cause: {}", e.getMessage());
		}
	}
}
