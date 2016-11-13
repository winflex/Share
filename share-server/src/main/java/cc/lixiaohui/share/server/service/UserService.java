package cc.lixiaohui.share.server.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.TimeUtils;

/**
 * @author lixiaohui
 * @date 2016年11月7日 下午9:46:23
 */
public class UserService extends AbstractService{
	
	public UserService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * <pre>
	 * int userId
	 * {
	 * 	"userId":321                      # 用户ID
	 *  "username":"lixiaohui",           # 用户名
	 *  "sex":"男",                        # 性别
	 *  "signature":"我是好人",              # 个性签名
	 *  "role":{
	 *       "id":32,                      # 角色ID
	 *       "description":"管理员"          # 角色描述
	 *  },
	 *  "registerTime":1478666778,        # 注册时间
	 *  "headImageId":12                  # 头像ID
	 * }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	public String getUser() throws ServiceException{
		int userId = 0;
		try {
			userId = getIntParameter("userId");
		} catch (Exception e) {
			JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
		
		try {
			UserDao dao = daofactory.getDao(UserDao.class);
			User user = dao.getById(userId);
			if (user == null) { // 用户不存在
				return JSONUtils.newFailureResult("未找到ID为" + userId + "的用户", ErrorCode.RESOURCE_NOT_FOUND, "");
			}
			// 封装JSON返回
			JSONObject result = packSingleUser(user);
			return JSONUtils.newSuccessfulResult("获取用户成功", result);
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}

	/**
	 * <pre>
	 * String password, String sex, String signature, int headImageId
	 * {}
	 * </pre>
	 * @return
	 */
	public String updateUser() {
		
		return null;
	}
	
	/**
	 * int userId
	 * <pre>
	 * {}
	 * </pre>
	 * @return
	 */
	public String shield() {
		return null;
	}
	
	/**
	 * <pre>
	 * int userId, int start, int limit
	 * {
	 *       "count":2,
	 *       "list":
	 *         [
	 *           {
	 *             "id":21,                    # 好友的用户ID
	 *             "time":1466757677,          # 成为好友的时间
	 *             "username":"lisi",          # 好友用户名
	 *             "sex":"男",
	 *             "signature":"",
	 *             "role":
	 *               {
	 *                 "id":21,
	 *                 "description":"管理员"    # 角色描述
	 *               }
	 *             "registerTime":144342342    # 用户注册时间
	 *             "headImageId":13            # 头像ID
	 *           },
	 *           {
	 *             "id":32,                    # 好友的用户ID
	 *             "time":1466757677,          # 成为好友的时间
	 *             "username":"lisi",          # 好友用户名
	 *             "sex":"男",
	 *             "signature":"",
	 *             "role":
	 *               {
	 *                 "id":21,
	 *                 "description":"管理员"    # 角色描述
	 *               }
	 *             "registerTime":144342342    # 用户注册时间
	 *             "headImageId":13            # 头像ID
	 *           }
	 *         ]
	 *     }
	 * </pre>
	 * @return
	 */
	public String getFriends() {
		return null;
	}
	
	/**
	 * int friendId
	 * {}
	 * @param ctx
	 * @param friendId
	 * @return
	 */
	public String deleteFriend() {
		return null;
	}
	
	// -------------------- util methods ------------------
	
	private JSONObject packSingleUser(User user) {
		JSONObject result = new JSONObject();
		result.put("userId", user.getId());
		result.put("username", user.getUsername());
		result.put("sex", user.getSex());
		result.put("signature", user.getSignature());
		result.put("registerTime", TimeUtils.toLong(user.getRegisterTime()));
		result.put("headImageId", user.getHeadImageId());
		
		JSONObject role = new JSONObject();
		role.put("id", user.getRoleId());
		role.put("description", user.getRole().getDescription());
		
		result.put("role", role);
		return result;
	}
}
