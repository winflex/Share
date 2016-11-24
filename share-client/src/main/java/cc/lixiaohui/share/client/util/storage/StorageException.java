package cc.lixiaohui.share.client.util.storage;

import java.io.IOException;

/**
 * @author lixiaohui
 * @date 2016年11月23日 下午7:34:31
 */
public class StorageException extends IOException {

	private static final long serialVersionUID = 6475519912126993789L;

	public StorageException() {
		super();
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageException(String message) {
		super(message);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}

}
