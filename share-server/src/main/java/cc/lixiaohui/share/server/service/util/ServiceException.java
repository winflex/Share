package cc.lixiaohui.share.server.service.util;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午9:16:27
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 3326807719113405833L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}
