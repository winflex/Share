package cc.lixiaohui.share.server.service;

import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.server.core.Session;
import cc.lixiaohui.share.server.model.bean.ForbidenWord;
import cc.lixiaohui.share.server.model.dao.ForbidenWordDao;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
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
@Service(name = "ForbidenWordService")
public class ForbidenWordService extends AbstractService {

	public ForbidenWordService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * 获取敏感词汇集合
	 * int start, int limit 
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
	@Procedure(name = "getForbidenWords", level=PrivilegeLevel.ADMIN)
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
	 * 获取逻辑删除的敏感词
	 * int start, int limit
	 * @param start
	 * @param limit
	 * @return
	 */
	@Procedure(name = "getDeletedForbidenWords", level=PrivilegeLevel.ADMIN)
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
	 * 逻辑删除敏感词
	 * int id
	 * {}
	 * 
	 * @param id
	 * @return
	 */
	@Procedure(name = "deleteForbidenWord", level=PrivilegeLevel.ADMIN)
	public String deleteForbidenWord() {
		int id;
		try {
			id = getIntParameter("wordId");
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.PARAMETER, t);
		}
		
		try {
			ForbidenWordDao dao = daofactory.getDao(ForbidenWordDao.class);
			int result = dao.vitualDelete(id);
			if (result > 0) {
				session.getSessionManager().getWordFilter().getHolder().unableWord(id);
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
	 * 恢复已逻辑删除的敏感词
	 * int id
	 * @param id
	 * @return
	 */
	@Procedure(name = "recoverForbidenWord", level=PrivilegeLevel.ADMIN)
	public String recoverForbidenWord() {
		int id;
		try {
			id = getIntParameter("wordId");
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.PARAMETER, t);
		}
		
		try {
			ForbidenWordDao dao = daofactory.getDao(ForbidenWordDao.class);
			int result = dao.recover(id);
			if (result > 0) {
				session.getSessionManager().getWordFilter().getHolder().enableWord(id);
				return JSONUtils.newSuccessfulResult("恢复成功");
			} else {
				return JSONUtils.newFailureResult("恢复失败", ErrorCode.UNKOWN, "");
			}
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.DATABASE, e);
		}
	}
	
	@Procedure(name = "addForbidenWord", level=PrivilegeLevel.ADMIN)
	public String addForbidenWord() throws ServiceException {
		try {
			String content = getStringParameter("content");
			ForbidenWordDao dao = daofactory.getDao(ForbidenWordDao.class);
			ForbidenWord word = new ForbidenWord(content);
			if (dao.add(word) > 0) {
				JSONObject result = new JSONObject();
				result.put("id", word.getId());
				
				session.getSessionManager().getWordFilter().getHolder().addWord(word);
				
				return JSONUtils.newSuccessfulResult("添加成功", result);
			} else {
				return JSONUtils.newFailureResult("添加失败", ErrorCode.DATABASE, "");
			}
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause);
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