package cc.lixiaohui.share.client.util.storage.entity;

/**
 * @author lixiaohui
 * @date 2016年11月23日 下午8:38:14
 */
public class StorageEntity {
	
	public static enum StorageStatus {
		TRANSIENT, // not
		PERSISTENT
	}
	
	protected StorageStatus status;
	
	public StorageStatus getStatus() {
		return status;
	}
	
	public StorageEntity setStatus(StorageStatus status) {
		this.status = status;
		return this;
	}
}
