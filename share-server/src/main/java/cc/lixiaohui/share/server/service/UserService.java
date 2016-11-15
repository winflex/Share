package cc.lixiaohui.share.server.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cc.lixiaohui.share.model.bean.Picture;
import cc.lixiaohui.share.model.bean.Role;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.PictureDao;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.EncryptUtils;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.TimeUtils;

/**
 * TODO getFriends, deleteFriend
 * @author lixiaohui
 * @date 2016年11月7日 下午9:46:23
 */
@Service(name = "UserService")
public class UserService extends AbstractService{
	
	public UserService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * @param userId int, !nullable
	 * @return
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
	 *  "selfShield":false,					# 自己是否屏蔽了自己
	 *  "adminShield":false					# 管理员是否屏蔽了自己
	 * }
	 * </pre>
	 * @throws ServiceException
	 */
	@Procedure(name = "getUser")
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
	 * <b><em>Node: need to be logged to perform this operation</em><b>
	 * 
	 * @param password String, nullable
	 * @param sex String, nullable
	 * @param signature String, nullable
	 * @param headImageId, nullable
	 * @return {}
	 */
	@Procedure(name = "updateUser")
	public String updateUser() {
		int userId = session.getUserId(); // 从session中取userId
		
		String password = null;
		String sex = null;
		String signature = null;
		int headImageId;
		try {
			password = getStringParameter("password", null);
			sex = getStringParameter("sex", null);
			signature = getStringParameter("signature", null);
			headImageId = getIntParameter("headImageId", -1);
		} catch (Throwable e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
		
		try {
			// 检查新头像是否存在
			PictureDao pictureDao = daofactory.getDao(PictureDao.class);
			Picture newHeadImage = null;
			if (headImageId != -1) {
				newHeadImage = pictureDao.getById(headImageId);
			}
			if (newHeadImage == null) {
				return JSONUtils.newFailureResult("图片 " + headImageId + " 不存在", ErrorCode.UNKOWN, "");
			}
			// 获取当前用户
			UserDao dao = daofactory.getDao(UserDao.class);
			User newUser = User.copy(dao.getById(userId));
			
			// 设新属性
			newUser.setHeadImage(newHeadImage);
			if (password != null) {
				newUser.setPassword(EncryptUtils.md5(password));
			}
			if (sex != null) {
				newUser.setSex(sex);
			}
			if (signature != null) {
				newUser.setSignature(signature);
			}
			// 更新
			if (dao.update(newUser) > 0) {
				return JSONUtils.newSuccessfulResult("更新成功");
			} else {
				return JSONUtils.newFailureResult("更新失败", ErrorCode.UNKOWN, "");
			}
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}
	
	/**
	 * 屏蔽用户, 分为管理员屏蔽和自屏蔽
	 * 判断userId是否和session中的userId一致, 若一致则说明是selfShield, 否则就是adminShield, 此时应该判断是否是管理员
	 * @param userId int, !nullable
	 * @return {}
	 */
	@Procedure(name = "shield")
	public String shield() {
		int userId;
		try {
			userId = getIntParameter("userId");
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
		
		try {
			UserDao dao = daofactory.getDao(UserDao.class);
			User user = null;
			// selfShield
			if (userId == session.getUserId()) {
				user = dao.getById(userId);
				user.setSelfForbid(true);
			} else { // adminSheild
				// TODO see if is administrator
				if (!Role.isAdmin(dao.getById(session.getUserId()).getRole().getId())) { // no 
					return JSONUtils.newFailureResult("非管理员无法屏蔽他人", ErrorCode.AUTH, "");
				}
				// TODO 执行管理员屏蔽
				user = dao.getById(userId);
				user.setAdminForbid(true);
			}
			// 执行物理更新
			if (dao.update(user) > 0) {
				// update session
				session.setAdminShield(user.isAdminForbid());
				session.setSelfShield(user.isSelfForbid());
				return JSONUtils.newSuccessfulResult("屏蔽成功");
			} else {
				return JSONUtils.newFailureResult("屏蔽失败", ErrorCode.UNKOWN	, "");
			}
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
	}
	
	/**
	 * 取消屏蔽, 分自己取消屏蔽和管理员取消屏蔽
	 * {}
	 * @param userId, !nullable
	 * @return {}
	 * 
	 */
	@Procedure(name = "unshield")
	public String unshield() {
		int userId;
		try {
			userId = getIntParameter("userId");
		} catch (Throwable t) {
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
		
		try {
			UserDao dao = daofactory.getDao(UserDao.class);
			User user = null;
			if (userId == session.getUserId()) { // self
				user = dao.getById(userId);
				user.setSelfForbid(false);
			} else {
				// TODO is admin
				if (!Role.isAdmin(dao.getById(session.getUserId()).getRole().getId())) { // no 
					return JSONUtils.newFailureResult("非管理员无法取消屏蔽他人", ErrorCode.AUTH, "");
				}
				user = dao.getById(userId);
				user.setAdminForbid(false);
			}
			// 物理更新
			if (dao.update(user) > 0) {
				session.setAdminShield(user.isAdminForbid());
				session.setSelfShield(user.isSelfForbid());
				return JSONUtils.newSuccessfulResult("取消屏蔽成功");
			} else {
				return JSONUtils.newFailureResult("取消屏蔽失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
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
	@Procedure(name = "getFriends")
	public String getFriends() {
		return null;
	}
	
	/**
	 * @param friendId int, !nullable
	 * @return {}
	 */
	@Procedure(name = "deleteFriends")
	public String deleteFriend() {
		return null;
	}
	
	// -------------------- util methods ------------------
	
	private JSONObject packSingleUser(User user) {
		JSONObject result = new JSONObject();
		
		JSONObject role = new JSONObject();
		role.put("id", user.getRole().getId());
		role.put("description", user.getRole().getDescription());
		
		result.put("role", role);
		result.put("userId", user.getId());
		result.put("username", user.getUsername());
		result.put("sex", user.getSex());
		result.put("signature", user.getSignature());
		result.put("registerTime", TimeUtils.toLong(user.getRegisterTime()));
		result.put("headImageId", user.getHeadImage().getId());
		result.put("selfShield", user.isSelfForbid());
		result.put("adminShield", user.isAdminForbid());
		return result;
	}
}
