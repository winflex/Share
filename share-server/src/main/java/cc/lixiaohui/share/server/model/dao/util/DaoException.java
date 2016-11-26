package cc.lixiaohui.share.server.model.dao.util;

/**
 * @author lixiaohui
 * @date 2016年11月12日 下午12:40:32
 */
public class DaoException extends Exception {

	private static final long serialVersionUID = -4976586487884627943L;

	public DaoException() {
		super();
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

}
