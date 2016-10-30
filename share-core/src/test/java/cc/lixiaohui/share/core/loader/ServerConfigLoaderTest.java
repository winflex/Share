package cc.lixiaohui.share.core.loader;

import org.junit.Test;

import cc.lixiaohui.share.core.config.ConfigException;
import cc.lixiaohui.share.core.config.ServerConfig;
import cc.lixiaohui.share.core.config.loader.ServerConfigLoader;

/**
 * @author lixiaohui
 * @date 2016年10月30日 上午1:03:48
 */
public class ServerConfigLoaderTest {
	
	String path = "cc/lixiaohui/share/core/config/server.xml";
	
	@Test
	public void test() throws ConfigException {
		ServerConfigLoader loader = new ServerConfigLoader(path);
		ServerConfig config = loader.load();
		System.out.println();
	}
	
}
