package cc.lixiaohui.share.server.config;

/**
 * @author lixiaohui
 * @date 2016年10月30日 上午12:53:46
 */
public class ConfigException extends Exception {

	private static final long serialVersionUID = -973201644481605655L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}
	
}
