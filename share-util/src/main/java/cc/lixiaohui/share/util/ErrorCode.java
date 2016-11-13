package cc.lixiaohui.share.util;

import java.io.IOError;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 错误码
 * @author lixiaohui
 * @date 2016年11月11日 下午11:38:32
 */
public enum ErrorCode {
	
	UNKOWN(0),
	
	DATABASE(1),
	
	IO(2),
	
	AUTH(3),
	
	PARAMETER(4),
	
	RESOURCE_NOT_FOUND(5);
	
	private int value;
	
	ErrorCode(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ErrorCode wrap(Throwable t) {
		if (t instanceof SQLException) {
			return DATABASE;
		} 
		
		if (t instanceof IOException || t instanceof IOError) {
			return IO;
		}
		
		if (t instanceof IllegalArgumentException || t instanceof NumberFormatException) {
			return PARAMETER;
		}
		return UNKOWN;
	}
}