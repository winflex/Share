package cc.lixiaohui.share.server.service;

import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.protocol.PushMessage.Type;
import cc.lixiaohui.share.server.core.Session;
import cc.lixiaohui.share.server.model.bean.FriendShip;
import cc.lixiaohui.share.server.model.bean.Picture;
import cc.lixiaohui.share.server.model.bean.Role;
import cc.lixiaohui.share.server.model.bean.User;
import cc.lixiaohui.share.server.model.dao.FriendShipDao;
import cc.lixiaohui.share.server.model.dao.PictureDao;
import cc.lixiaohui.share.server.model.dao.UserDao;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.SQLUtils;
import cc.lixiaohui.share.util.TimeUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
	 * 获取用户详细信息
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
	 * 获取用户列表
	 * @param start int, nullable
	 * @param limit int, nullable
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name="getUsers", level=PrivilegeLevel.ADMIN)
	public String getUsers() throws ServiceException {
		try {
			int start = getIntParameter("start");
			int limit = getIntParameter("limit");
			
			UserDao dao = daofactory.getDao(UserDao.class);
			List<User> users = dao.listSome(start, limit);
			return JSONUtils.newSuccessfulResult("获取用户成功", packUsersResult(users));
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause);
		}
	}
	
	private JSONObject packUsersResult(List<User> users) {
		JSONArray items = new JSONArray();
		for (User user : users) {
			items.add(packSingleUser(user));
		}
		JSONObject result = new JSONObject();
		result.put("count", users.size());
		result.put("list", items);
		return result;
	}

	/**
	 * 搜索用户
	 * 
	 * @param keyword string, !nullable
	 * @param start int
	 * @param limit int 
	 * @return
	 */
	@Procedure(name = "searchUser")
	public String searchUser() {
		try {
			String keyword = SQLUtils.toLike(getStringParameter("keyword"));
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			
			UserDao userDao = daofactory.getDao(UserDao.class);
			List<User> users = userDao.search(SQLUtils.toLike(keyword), start, limit);
			return JSONUtils.newSuccessfulResult("搜索到" + users.size() + "个用户", packSearchResult(users));
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
	}
	
	/**
	 * 更新用户信息
	 * <b><em>Node: need to be logged to perform this operation</em><b>
	 * 
	 * @param password String, nullable
	 * @param sex String, nullable
	 * @param signature String, nullable
	 * @param headImageId, nullable
	 * @return {}
	 */
	@Procedure(name = "updateUser", level=PrivilegeLevel.LOGGED)
	public String updateUser() {
		int userId = session.getUser().getId();
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
			UserDao dao = daofactory.getDao(UserDao.class);
			User newUser = User.copy(dao.getById(userId));
			
			newUser.setHeadImage(newHeadImage);
			if (password != null) {
				//newUser.setPassword(EncryptUtils.md5(password));
				newUser.setPassword(password);
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
	@Procedure(name = "shield", level=PrivilegeLevel.LOGGED)
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
			if (userId == session.getUser().getId()) {
				user = dao.getById(userId);
				user.setSelfForbid(true);
			} else { // adminSheild
				// TODO see if is administrator
				if (!Role.isAdmin(dao.getById(session.getUser().getId()).getRole().getId())) { // no 
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
	@Procedure(name = "unshield", level=PrivilegeLevel.LOGGED)
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
			if (userId == session.getUser().getId()) { // self
				user = dao.getById(userId);
				user.setSelfForbid(false);
			} else {
				// TODO is admin
				if (!Role.isAdmin(dao.getById(session.getUser().getId()).getRole().getId())) { // no 
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
	 * 添加好友请求
	 * @param targetUserId int, !nullable
	 * @return
	 */
	@Procedure(name = "addFriend", level=PrivilegeLevel.LOGGED)
	public String addFriend() {
		if (!session.isLogined()) {
			return JSONUtils.newFailureResult("您未登陆", ErrorCode.AUTH, "");
		}
		try {
			int targetUserId = getIntParameter("targetUserId");
			
			if (targetUserId == session.getUser().getId()) {
				return JSONUtils.newFailureResult("不能添加自己为好友", ErrorCode.AUTH, "");
			}
			
			UserDao dao = daofactory.getDao(UserDao.class);
			FriendShipDao fsDao = daofactory.getDao(FriendShipDao.class);
			User fromUser = dao.getById(session.getUser().getId());
			User toUser = dao.getById(targetUserId);
			
			if (toUser == null) {
				return JSONUtils.newFailureResult("目标用户不存在", ErrorCode.RESOURCE_NOT_FOUND, "");
			}
			
			FriendShip fs = new FriendShip();
			fs.setAskUser(fromUser);
			fs.setAskedUser(toUser);
			if (fsDao.add(fs) > 0) {
				// TODO 推送
				try {
					System.out.println(packAddFriendPush(fs).toJSONString());
					PushMessage message = PushMessage.builder().type(Type.FRI_REQ)
							.pushData(packAddFriendPush(fs).toJSONString()).build();
					session.getSessionManager().pushTo(fs.getAskedUser().getId(), message);
				} catch (Throwable t) {
					logger.error("{}", t);
				}
				return JSONUtils.newSuccessfulResult("请求已发送");
			} else {
				return JSONUtils.newFailureResult("请求发送失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
	}
	
	/**
	 * 处理好友请求
	 * @param friendShipId int, !nullable
	 * @param accept boolean, !nullable
	 * @throws ServiceException
	 */
	@Procedure(name="handleFriendRequest", level=PrivilegeLevel.LOGGED)
	public String handleFriendRequest() throws ServiceException {
		try {
			int friendShipId = getIntParameter("friendShipId");
			boolean accept = getBooleanParameter("accept");
			
			FriendShipDao dao = daofactory.getDao(FriendShipDao.class);
			FriendShip fs = dao.getById(friendShipId);
			if (fs == null) {
				return JSONUtils.newFailureResult("好友关系不存在", ErrorCode.RESOURCE_NOT_FOUND, "");
			}
			
			if (fs.getAskedUser().getId() != session.getUser().getId()) {
				return JSONUtils.newFailureResult("参数错误", ErrorCode.PARAMETER, "");
			}
			
			if (!fs.isPending()) {
				return JSONUtils.newFailureResult("不能重复确认", ErrorCode.SERVICE, "");
			}
			
			if (accept) { // 好友确立
				fs.setAnswerTime(TimeUtils.currentTimestamp());
				fs.setPending(false);
				if (dao.update(fs) > 0) {
					// 推送
					try {
						//System.out.println(packFriendShipResp(fs, accept).toJSONString());
						PushMessage message = PushMessage.builder().type(Type.FRI_RESP)
								.pushData(packFriendShipResp(fs, accept).toJSONString()).build();
						session.getSessionManager().pushTo(fs.getAskUser().getId(), message);
					} catch (Throwable cause) {
						logger.error("{}", cause);
					}
					return JSONUtils.newSuccessfulResult("成为好友");
				} else {
					return JSONUtils.newFailureResult("请求发送失败", ErrorCode.DATABASE, "");
				}
				
			} else {
				// 删除该friendShip, 并推送对方
				if (dao.delete(fs) > 0) {
					try {
						//System.out.println(packFriendShipResp(fs, accept).toJSONString());
						PushMessage message = PushMessage.builder().type(Type.FRI_RESP)
								.pushData(packFriendShipResp(fs, accept).toJSONString()).build();
						session.getSessionManager().pushTo(fs.getAskUser().getId(), message);
					} catch (Throwable cause) {
						logger.error("{}", cause);
					}
					return JSONUtils.newSuccessfulResult("已拒绝");
				} else {
					return JSONUtils.newFailureResult("请求发送失败", ErrorCode.DATABASE, "");
				}
			}
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause);
		}
	}

	/**
	 * 获取好友
	 * @param start int, nullable
	 * @param limit int, nullable
	 * <pre>
	 * int start, int limit
	 * {
	 *       "count":2,
	 *       "list":
	 *         [
	 *           {
	 *             "id":1						# 好友关系ID
	 *              "userIdd":1						# 好友关系ID
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
	 *              "id":1						# 好友关系ID
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
	@Procedure(name = "getFriends", level=PrivilegeLevel.LOGGED)
	public String getFriends() {
		try {
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			
			FriendShipDao dao = daofactory.getDao(FriendShipDao.class);
			List<Object[]> friendships = dao.getFriends(session.getUser().getId(), start, limit);
			return JSONUtils.newSuccessfulResult("获取好友成功", packFriends(session.getUser().getId(), friendships));
			
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause.getMessage(), ErrorCode.wrap(cause), cause);
		}
	}
	
	/**
	 * 删除好友
	 * @param friendShipId int, !nullable
	 * @return {}
	 */
	@Procedure(name = "deleteFriend", level=PrivilegeLevel.LOGGED)
	public String deleteFriend() {
		try {
			int userId = session.getUser().getId();
			int friendShipId = getIntParameter("friendShipId");
			
			FriendShipDao dao = daofactory.getDao(FriendShipDao.class);
			//int result = dao.deleteFriend(userId, friendShipId);
			FriendShip fs = dao.getById(friendShipId);
			if (fs == null) {
				return JSONUtils.newFailureResult("好友不存在", ErrorCode.RESOURCE_NOT_FOUND, "");
			}
			if (fs.getAskedUser().getId() == userId || fs.getAskedUser().getId() == userId) {
				if (dao.delete(fs) > 0) {
					// TODO 推送
					try {
						boolean inverse = fs.getAskedUser().getId() == userId;
						PushMessage message = PushMessage.builder().type(Type.FRI_REQ)
								.pushData(packDeleteFriendPush(fs, inverse).toJSONString()).build();
						session.getSessionManager().pushTo(inverse ? fs.getAskedUser().getId() : fs.getAskUser().getId(), message);
					} catch (Throwable cause) {
						logger.error("{}", cause);
					}
					return JSONUtils.newSuccessfulResult("删除好友成功");
				} else {
					JSONUtils.newFailureResult("删除好友失败", ErrorCode.DATABASE, "");
				}
			}
			return JSONUtils.newFailureResult("删除好友失败", ErrorCode.AUTH, "");
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause.getMessage(), ErrorCode.wrap(cause), cause);
		}
	}
	
	// -------------------- util methods ------------------
	// inverse指明当前用户是否是好友关系的主动方
	private JSON packDeleteFriendPush(FriendShip fs, boolean inverse) {
		JSONObject result = new JSONObject();
		result.put("user", packSingleUser(inverse ? fs.getAskUser() : fs.getAskedUser()));
		return result;
	}

	private JSONObject packSearchResult(List<User> users) {
		JSONArray userArray = new JSONArray();
		for (User user : users) {
			userArray.add(packSingleUser(user));
		}
		
		JSONObject result = new JSONObject();
		result.put("count", users.size());
		result.put("users", userArray);
 		return result;
	}
	
	static JSONObject packSingleUser(User user) {
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
		result.put("headImageId", user.getHeadImage() == null ? -1 : user.getHeadImage().getId());
		result.put("selfShield", user.isSelfForbid());
		result.put("adminShield", user.isAdminForbid());
		return result;
	}
	
	private JSONObject packFriends(int userId, List<Object[]> friendships) {
		JSONArray array = new JSONArray();
		for (Object[] arr : friendships) {
			FriendShip fs = (FriendShip) arr[0];
			
			JSONObject friendShip = new JSONObject();
			friendShip.put("id", fs.getId());
			friendShip.put("time", fs.getAnswerTime());
			if (fs.getAskUser().getId() == userId) {
				friendShip.put("user", packSingleUser(fs.getAskedUser()));
			} else {
				friendShip.put("user", packSingleUser(fs.getAskUser()));
			}
			array.add(friendShip);
		}
		
		JSONObject result = new JSONObject();
		result.put("friends", array);
		return result;
	}

	private JSONObject packFriendShipResp(FriendShip fs, boolean accept) {
		JSONObject result = new JSONObject();
		result.put("accept", accept);
		result.put("friendShipId", fs.getId());
		result.put("user", packSingleUser(fs.getAskedUser()));
		return result;
	}

	private JSONObject packAddFriendPush(FriendShip fs) {
		JSONObject result = new JSONObject();
		result.put("friendShipId", fs.getId());
		result.put("user", packSingleUser(fs.getAskUser()));
		return result;
	}
}
