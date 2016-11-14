package cc.lixiaohui.share.client;

/**
 * @author lixiaohui
 * @date 2016年11月13日 下午7:47:26
 */
public class ShareClientException extends Exception {

	private static final long serialVersionUID = 9020600124469994912L;

	public ShareClientException() {
		super();
	}

	public ShareClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShareClientException(String message) {
		super(message);
	}

	public ShareClientException(Throwable cause) {
		super(cause);
	}
}
