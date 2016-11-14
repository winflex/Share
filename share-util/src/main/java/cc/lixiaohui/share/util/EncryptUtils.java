package cc.lixiaohui.share.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.alibaba.fastjson.util.Base64;

/**
 * @author lixiaohui
 * @date 2016年11月13日 下午4:54:01
 */
public final class EncryptUtils {
	
	private volatile static MessageDigest md5;
	
	private static MessageDigest getMD5() {
		if (md5 == null) {
			synchronized (EncryptUtils.class) {
				try {
					md5 = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return md5;
	}
	
	/**
	 * md5
	 * @param raw
	 * @return
	 */
	public static String md5(String raw) {
		raw = Objects.requireNonNull(raw);
		return new String(getMD5().digest(raw.getBytes()));
	}
	
}
