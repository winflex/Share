package cc.lixiaohui.share.client.util.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import cc.lixiaohui.share.client.util.storage.entity.Picture;
import cc.lixiaohui.share.client.util.storage.entity.StorageEntity;
import cc.lixiaohui.share.client.util.storage.entity.StorageEntity.StorageStatus;
import cc.lixiaohui.share.protocol.codec.serialize.HessianSerializer;
import cc.lixiaohui.share.util.FileUtils;
import cc.lixiaohui.share.util.UUIDUtils;

/**
 * @author lixiaohui
 * @date 2016年11月23日 下午7:15:06
 */
public class LocalStorage {
	
	private static enum Status {
		OPENED,
		CLOSED
	}
	
	private volatile Status status;
	
	private String basePath;
	
	private Map<Integer, Picture> pictures = new ConcurrentHashMap<Integer, Picture>();
	
	private Map<Integer, String> pictureFilePaths = new ConcurrentHashMap<Integer, String>();
	
	private ExecutorService executor;
	
	private HessianSerializer serializer = new HessianSerializer();
	
	private static final String MAPPER_FILE_NAME = "mapper";
	
	private static final int MAX_CACHES = 20; 
	
	private LocalStorage(String basePath, ExecutorService executor) {
		this.basePath = basePath;
		this.status = Status.OPENED;
		this.executor = executor;
	
		try {
			byte[] bytes = FileUtils.getResourceAsBytes(basePath + File.separator + MAPPER_FILE_NAME);
			pictureFilePaths = serializer.deserialize(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static LocalStorage open(String path, ExecutorService executor) throws StorageException {
		return new LocalStorage(path, executor);
	}
	
	/**
	 * 缓存图片, 不写到硬盘
	 * @param pictureId
	 * @param bytes
	 * @param suffix
	 * @return
	 */
	public void store(int pictureId, byte[] bytes) {
		checkIfClosed();
		
		store0(pictureId, bytes, true);
	}

	private void store0(int pictureId, byte[] bytes, boolean flushIfOverflow) {
		if (pictures.containsKey(pictureId)) {
			Picture p = pictures.get(pictureId);
			p.setBytes(bytes);
			p.setStatus(StorageStatus.TRANSIENT);
			return;
		}
		
		Picture p = new Picture(pictureId, bytes, generateName());
		p.setStatus(StorageEntity.StorageStatus.TRANSIENT);
		pictures.put(pictureId, p);
		pictureFilePaths.put(pictureId, p.getFile());
	}
	
	/**
	 * 缓存图片, 写到硬盘
	 * @param pictureId
	 * @param bytes
	 */
	public void storePictureAndFlush(int pictureId, byte[] bytes) {
		checkIfClosed();
		store0(pictureId, bytes, false);
		doFlush(pictures.size() > MAX_CACHES);
	}
	
	/**
	 * 缓存多张图片, 不写到硬盘
	 * @param pictures
	 */
	public void storePictures(Map<Integer, byte[]> pictures) {
		for (Map.Entry<Integer, byte[]> entry : pictures.entrySet()) {
			store0(entry.getKey(), entry.getValue(), false);
		}
		if (pictures.size() > MAX_CACHES) {
			doFlush(true);
		}
	}
	
	/**
	 * 缓存多张图片, 写到硬盘
	 * @param pictures
	 */
	public void storePicturesAndFlush(Map<Integer, byte[]> pictures) {
		for (Map.Entry<Integer, byte[]> entry : pictures.entrySet()) {
			store0(entry.getKey(), entry.getValue(), false);
		}
		doFlush(pictures.size() > MAX_CACHES);
	}

	/**
	 * 获取单张图片
	 * @param pictureId
	 * @return
	 */
	public byte[] getPicture(int pictureId) {
		checkIfClosed();
		Picture picture = pictures.get(pictureId);
		if (picture == null) {
			String file = pictureFilePaths.get(pictureId);
			if (file == null) {
				return null;
			}
			try {
				byte[] bytes = FileUtils.getResourceAsBytes(file);
				Picture p = new Picture(pictureId, bytes, file);
				p.setStatus(StorageEntity.StorageStatus.PERSISTENT);
				pictures.put(pictureId, p);
				return bytes;
			} catch (IOException e) {
				return null;
			}
			
		} else {
			return picture.getBytes();
		}
	}

	/**
	 * 获取多张图片
	 * @param pictureIds
	 * @return
	 */
	public Map<Integer, byte[]> getPictures(int[] pictureIds) {
		checkIfClosed();
		Map<Integer, byte[]> map = new HashMap<Integer, byte[]>();
		for (int id : pictureIds) {
			byte[] bytes = getPicture(id);
			if (bytes != null) {
				map.put(id, bytes);
			}
		}
		return map;
	}
	
	public void flush() throws StorageException {
		checkIfClosed();
		doFlush(false);
	}

	public void close() throws StorageException {
		doFlush(true);
		
		try {
			byte[] bytes = serializer.serialize(pictureFilePaths);
			FileUtils.saveFile(basePath, MAPPER_FILE_NAME, bytes);
		} catch (IOException e) {
			throw new StorageException(e);
		}
		status = Status.CLOSED;
	}

	public String getPath() {
		return basePath;
	}
	
	private String generateName() {
		return basePath + File.separator + UUIDUtils.random32Byte();
	}
	
	private void checkIfClosed() {
		if (status != Status.OPENED) {
			throw new IllegalStateException(status.name());
		}
	}
	
	private void doFlush(boolean clearCache) {
		executor.submit(new FlushTask(clearCache));
	}
	
	private class FlushTask implements Runnable {

		private boolean clearCache;
		
		public FlushTask(boolean clearCache) {
			this.clearCache = clearCache;
		}
		
		
		@Override
		public void run() {
			
			Iterator<Map.Entry<Integer, Picture>> it = pictures.entrySet().iterator();
			while (it.hasNext()) {
				Picture picture = it.next().getValue();
				
				if (picture.getStatus() == StorageStatus.PERSISTENT) {
					continue;
				}
				
				try {
					FileUtils.saveFile(picture.getFile(), picture.getBytes());
					picture.setStatus(StorageStatus.PERSISTENT);
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
				
				if (clearCache) {
					it.remove();
				}
			}
		}
		
	}
	
}
