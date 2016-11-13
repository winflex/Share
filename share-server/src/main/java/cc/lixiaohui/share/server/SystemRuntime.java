package cc.lixiaohui.share.server;

import cc.lixiaohui.share.util.FileUtils;


/**
 * @author lixiaohui
 * @date 2016年11月10日 下午7:58:08
 */
public class SystemRuntime {
	
	public static final String SERVER_HOME = "server.home";
	
	public static final String DIR_CONF = "conf";
	public static final String DIR_DATA = "data";
	public static final String DIR_PICTURE = "pictures";
	
	public static final String CONF_NAME = "server.xml";	
	
	public static String serverHome() {
		return System.getProperty(SERVER_HOME);
	}
	
	public static final String picturePath() {
		return FileUtils.concatPath(serverHome(), DIR_DATA, DIR_PICTURE);
	}
}
