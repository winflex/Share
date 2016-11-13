package cc.lixiaohui.share.client;

/**
 * @author lixiaohui
 * @date 2016年10月31日 下午4:17:55
 */
public class ShareClientException extends Exception {

	private static final long serialVersionUID = 9020600124469994912L;

	public static enum Reason {
		/**
		 * 连接断开了
		 */
		CONNECTION_CLOSED,
		
		/**
		 * 服务器维修中
		 */
		SERVER_BEING_REPAIRED
	}
	
	private Reason reason;
	
	public ShareClientException() {
		super();
	}
	
	public ShareClientException(Reason reason) {
		this.reason = reason;
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

	/**
	 * @return the reason
	 */
	public Reason getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(Reason reason) {
		this.reason = reason;
	}

}
