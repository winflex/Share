package cc.lixiaohui.share.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:54:08
 */
public class JSONUtils {
	
	public static final String K_STATUS = "status";
	public static final String K_TIMESTAMP = "timestamp";
	public static final String K_MSG = "msg";
	public static final String K_RESULT = "result";
	public static final String K_ERRMSG = "errmsg";
	public static final String K_ERRCODE = "errcode";
	public static final String K_EXPMSG = "expmsg";
	
	public static final int V_STATUS_OK = 0;
	public static final int V_STATUS_ERR = 1;
	
	public static final String V_MSG = "操作成功";
	
	public static String EMPTY_JSON = "{}";
	
	public static String newSuccessfulResult(String message) {
		return newSuccessfulResult(message, null);
	}
	
	public static String newSuccessfulResult(JSONObject result) {
		return newSuccessfulResult(V_MSG, result);
	}
	
	public static String newSuccessfulResult(String message, JSONObject result) {
		JSONObject json = new JSONObject();
		json.put(K_STATUS, V_STATUS_OK);
		json.put(K_TIMESTAMP, TimeUtils.currentTimeMillis());
		json.put(K_MSG, message == null ? "" : message);
		json.put(K_RESULT, result == null ? EMPTY_JSON : result);
		return json.toJSONString();
	}
	
	public static String newFailureResult(String errorMessage, ErrorCode errorCode, String exceptionMessage) {
		return newFailureResult(errorMessage, errorCode, exceptionMessage, null);
	}
	
	public static String newFailureResult(String errMessage ,ErrorCode errorCode, String exceptionMessage, JSONObject result) {
		JSONObject json = new JSONObject();
		json.put(K_STATUS, V_STATUS_ERR);
		json.put(K_TIMESTAMP, TimeUtils.currentTimeMillis());
		json.put(K_ERRMSG, errMessage);
		json.put(K_ERRCODE, errorCode == null ? ErrorCode.UNKOWN.getValue() : errorCode.getValue());
		json.put(K_EXPMSG, exceptionMessage);
		if (result != null) {
			json.put(K_RESULT, result);
		}
		return json.toJSONString();
	}
	
	public static String newFailureResult(String errorMessage, ErrorCode errorCode, Throwable cause) {
		return newFailureResult(errorMessage, errorCode, ExceptionUtils.toDetail(cause));
	}
	
	public static void main(String[] args) {
		System.out.println(newSuccessfulResult("获取数据成功", new JSONObject()));
		System.out.println(newFailureResult("err", ErrorCode.UNKOWN, "dadsa"));
	}
}
