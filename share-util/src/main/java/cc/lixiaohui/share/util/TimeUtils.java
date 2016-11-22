package cc.lixiaohui.share.util;

import java.sql.Timestamp;

/**
 * @author lixiaohui
 * @date 2016年11月7日 下午11:51:44
 */
public class TimeUtils {
	
	public static long currentTimeMillis() {
		return System.currentTimeMillis();
	}
	
	public static Timestamp currentTimestamp() {
		return new Timestamp(currentTimeMillis());
	}
	
	public static long toLong(Timestamp time) {
		return time.getTime();
	}
	
	public static boolean isTimeout(long base, long timeout) {
		return base + timeout < System.currentTimeMillis();
	}
}
