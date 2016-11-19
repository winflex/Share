package cc.lixiaohui.share.util;

/**
 * @author lixiaohui
 * @date 2016年11月18日 下午5:05:30
 */
public class SQLUtils {
	
	public static final String toLike(String keyword) {
		return "%" + (keyword == null ? "" : keyword) +"%";
	}
	
}
