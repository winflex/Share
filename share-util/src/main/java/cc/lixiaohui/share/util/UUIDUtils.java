package cc.lixiaohui.share.util;

import java.util.UUID;

/**
 * @author lixiaohui
 * @date 2016年11月6日 下午3:34:19
 */
public class UUIDUtils {
	
	public static String random32Byte() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(random32Byte());
	}
}
