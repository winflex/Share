package cc.lixiaohui.share.server.service;

import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.model.bean.ForbidenWord;
import cc.lixiaohui.share.model.dao.ForbidenWordDao;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 敏感词相关操作实现
 * 
 * @author lixiaohui
 * @date 2016年11月12日 上午12:14:50
 */
public class ForbidenWordService extends AbstractService {

	public ForbidenWordService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * int start, int limit 获取所有敏感词汇集合
	 * 
	 * <pre>
	 * result json format:
	 * {
	 *  "count":2,
	 *  "words":[
	 *  	{
	 *  		"id":1,
	 *  		"content":"AV"  # 敏感词
	 *  	},
	 *  	{
	 *  		"id":2,
	 *  		"content":"VA"
	 *  	}
	 *  ]
	 * }
	 * </pre>
	 * @param start
	 * @param limit
	 * @return 
	 * @throws ServiceException
	 */
	public String getForbidenWords() {
		int start;
		int limit;
		try {
			start = getIntParameter("start");
			limit = getIntParameter("limit", 20);
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.PARAMETER, t);
		}
		
		try {
			ForbidenWordDao dao = daofactory.getDao(ForbidenWordDao.class);
			List<ForbidenWord> words = dao.listSomeUndeleted(start, limit);

			JSONObject result = packWords(words);
			return JSONUtils.newSuccessfulResult(result);
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.DATABASE, e);
		}
	}

	/**
	 * int start, int limit
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getDeletedForbidenWords() {
		int start;
		int limit;
		try {
			start = getIntParameter("start");
			limit = getIntParameter("limit", 20);
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.PARAMETER, t);
		}
		
		try {
			ForbidenWordDao dao = daofactory.getDao(ForbidenWordDao.class);
			List<ForbidenWord> words = dao.listSomeDeleted(start, limit);
			JSONObject result = packWords(words);
			return JSONUtils.newSuccessfulResult(result);
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.DATABASE, e);
		}
	}

	/**
	 * int id
	 * {}
	 * 
	 * @param id
	 * @return
	 */
	public String deleteForbidenWord() {
		
		int id;
		try {
			id = getIntParameter("id");
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.PARAMETER, t);
		}
		
		/*if (!session.isLogined()) {
			return JSONUtils.newFailureResult("无权限执行此操作", ErrorCode.AUTH, "");
		}*/
		
		try {
			ForbidenWordDao dao = daofactory.getDao(ForbidenWordDao.class);
			int result = dao.vitualDelete(id);
			if (result > 0) {
				return JSONUtils.newSuccessfulResult("删除成功");
			} else {
				return JSONUtils.newFailureResult("删除失败", ErrorCode.UNKOWN, "");
			}
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.DATABASE, e);
		}
	}

	/**
	 * int id
	 * @param id
	 * @return
	 */
	public String recoverForbidenWord() {
		int id;
		try {
			id = getIntParameter("id");
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.PARAMETER, t);
		}
		
		try {
			ForbidenWordDao dao = daofactory.getDao(ForbidenWordDao.class);
			int result = dao.recover(id);
			if (result > 0) {
				return JSONUtils.newSuccessfulResult("恢复成功");
			} else {
				return JSONUtils.newFailureResult("恢复失败", ErrorCode.UNKOWN, "");
			}
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.DATABASE, e);
		}
	}
	
	private JSONObject packWords(List<ForbidenWord> words) {
		JSONArray array = new JSONArray(words.size());
		for (ForbidenWord word : words) {
			JSONObject o = new JSONObject();
			o.put("id", word.getId());
			o.put("content", word.getContent());
			array.add(o);
		}

		JSONObject result = new JSONObject();
		result.put("count", words.size());
		result.put("words", array);
		return result;
	}

}