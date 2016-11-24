package cc.lixiaohui.share.client.util.storage.entity;

/**
 * @author lixiaohui
 * @date 2016年11月23日 下午8:42:23
 */
public class Picture extends StorageEntity{
	private int id;
	
	private byte[] bytes;
	
	private String file;
	
	public Picture() {
		super();
	}
	
	public Picture(int id, byte[] bytes, String file) {
		super();
		this.id = id;
		this.bytes = bytes;
		this.file = file;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public Picture setStatus(StorageStatus status) {
		return (Picture) super.setStatus(status);
	}
	
}
