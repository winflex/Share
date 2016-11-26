package cc.lixiaohui.share.server.service;

import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.model.bean.Sorting;
import cc.lixiaohui.share.server.model.dao.SortingDao;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 排序方式服务
 * 
 * @author lixiaohui
 * @date 2016年11月11日 下午10:28:55
 */
@Service(name = "SortingService")
public class SortingService extends AbstractService {

	public SortingService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * <pre>
	 *  {
	 *       "count":2,
	 *       "sortings":
	 *         [
	 *           {
	 *             "id":1,
	 *             "type":赞,
	 *             "description":"按点赞数正序"
	 *           },
	 *           {
	 *             "id":2,
	 *             "type":评论,
	 *             "description":"按评论数正序"
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "getSortings")
	public String getSortings() {
		try {
			SortingDao dao = daofactory.getDao(SortingDao.class);
			List<Sorting> sortings = dao.list();
			JSONArray array = new JSONArray(sortings.size());
			for (Sorting sorting : sortings) {
				JSONObject o = new JSONObject();
				o.put("id", sorting.getId());
				o.put("type", sorting.getType());
				o.put("description", sorting.getDescription());
				array.add(o);
			}
			
			JSONObject result = new JSONObject();
			result.put("count", sortings.size());
			result.put("sortings", array);
			return JSONUtils.newSuccessfulResult(result);
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult("获取数据发生错误", ErrorCode.wrap(e), e);
		}
	}
	
	/**
	 * 删除
	 * @param sortingId
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "deleteSorting", level=PrivilegeLevel.ADMIN)
	public String deleteSorting() throws ServiceException {
		int sortingId;
		try {
			sortingId = getIntParameter("sortingId");
		} catch (Throwable e) {
			logger.info("参数错误");
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
		
		try {
			SortingDao dao = daofactory.getDao(SortingDao.class);
			if (dao.vitualDelete(sortingId) > 0) { // 删除成功
				return JSONUtils.newSuccessfulResult("删除成功");
			} else {
				return JSONUtils.newFailureResult("删除失败", ErrorCode.UNKOWN, "");
			}
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult("访问数据库异常", ErrorCode.DATABASE, e);
		}
	}
}
