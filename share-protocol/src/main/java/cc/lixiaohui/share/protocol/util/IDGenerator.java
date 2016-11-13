package cc.lixiaohui.share.protocol.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午9:57:06
 */
public class IDGenerator {
	
	private static final AtomicLong GENERATOR = new AtomicLong(0);
	
	public static long generate() {
		return GENERATOR.getAndIncrement();
	}
}
