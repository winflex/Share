package cc.lixiaohui.share.client.util;

/**
 * @author lixiaohui
 * @date 2016年11月13日 下午7:47:26
 */
public class ClientException extends Exception {

	private static final long serialVersionUID = 9020600124469994912L;

	public ClientException() {
		super();
	}

	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientException(String message) {
		super(message);
	}

	public ClientException(Throwable cause) {
		super(cause);
	}
}
