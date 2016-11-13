package cc.lixiaohui.share.protocol;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午9:07:51
 */
public class MessageException extends Exception {

	private static final long serialVersionUID = 4440101449011773669L;

	public MessageException() {
		super();
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}
}
