package cc.lixiaohui.share.server.service;

import java.util.Map;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.model.bean.Role;
import cc.lixiaohui.share.server.model.bean.User;
import cc.lixiaohui.share.server.model.dao.RoleDao;
import cc.lixiaohui.share.server.model.dao.UserDao;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 处理登录注册注销
 * @author lixiaohui
 * @date 2016年11月11日 下午10:30:38
 */
@Service(name = "SessionService")
public class SessionService extends AbstractService {
	
	public static final String PATTERN_USERNAME = "";
	public static final String PATTERN_PASSWORD = "";

	public SessionService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * 注册
	 * <pre>
	 * {
	 * 		"userId":1,
	 * 		"role": {"roleId":1, "description":"管理员"},
	 * 		"selfShield":false,
	 * 		"adminShield":false
	 * }
	 * </pre>
	 * @param username String, !nullable
	 * @param password String, !nullable
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "register")
	public String register() throws ServiceException {
		
		String username = null;
		String password = null;
		try {
			username = getStringParameter("username");
			password = getStringParameter("password");
			//password = EncryptUtils.md5(password);// md5
		} catch (Exception e) {
			JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
		try {
			UserDao dao = daofactory.getDao(UserDao.class);
			if (dao.nameExist(username)) { // 用户名存在
				return JSONUtils.newFailureResult("用户名已存在", ErrorCode.NAME_EXIST, "");
			}
			// set role
			RoleDao roleDao = daofactory.getDao(RoleDao.class);
			User user = new User(username, password, roleDao.getById(Role.NORMAL));
			
			if (dao.add(user) > 0) {
				logger.debug("user registered {}", user);
				return JSONUtils.newSuccessfulResult("注册成功", packSuccess(user));
			} else {
				return JSONUtils.newFailureResult("注册失败", ErrorCode.UNKOWN, "");
			}
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}
	
	/**
	 * 添加管理员账号
	 * @param username String, !nullable
	 * @param password String, !nullable
	 * @return
	 * <pre>
	 * {
	 * 		"userId":1,
	 * 		"role": {"roleId":1, "description":"管理员"},
	 * 		"selfShield":false,
	 * 		"adminShield":false
	 * }
	 * </pre>
	 * @throws ServiceException
	 */
	@Procedure(name="registerAdmin", level=PrivilegeLevel.SUPER)
	public String registerAdmin() throws ServiceException {
		try {
			String username = getStringParameter("username");
			String password = getStringParameter("password");
			
			UserDao dao = daofactory.getDao(UserDao.class);
			if (dao.nameExist(username)) { // 用户名存在
				return JSONUtils.newFailureResult("用户名已存在", ErrorCode.NAME_EXIST, "");
			}
			// set role
			RoleDao roleDao = daofactory.getDao(RoleDao.class);
			Role role = roleDao.getById(Role.ADMIN);
			User user = new User(username, password, role);
			if (dao.add(user) > 0) {
				logger.debug("admin user registered {}", user);
				return JSONUtils.newSuccessfulResult("管理员账户添加成功", packSuccess(user));
			} else {
				return JSONUtils.newFailureResult("管理员账户添加失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable cause) {
			return JSONUtils.newFailureResult("添加管理员失败", ErrorCode.wrap(cause), cause);
		}
		
	}
	
	/**
	 * 登陆
	 * @param username String, !nullable
	 * @param password String, !nullable
	 * @return
	 * <pre>
	 * {
	 * 		"userId":1,
	 * 		"role": {"roleId":1, "description":"管理员"},
	 * 		"selfShield":false,
	 * 		"adminShield":false
	 * }
	 * </pre>
	 * @throws ServiceException
	 */
	@Procedure(name = "login")
	public String login() throws ServiceException {
		
		if (session.isLogined()) { // 已登陆
			return JSONUtils.newSuccessfulResult("您已经登陆", packSuccess(session.getUser()));
		}
		
		String username = null;
		String password = null;
		try {
			username = getStringParameter("username");
			password = getStringParameter("password");
			//password = EncryptUtils.md5(password);// md5
			
			UserDao dao = daofactory.getDao(UserDao.class);
			User user = dao.get(username, password);
			if (user == null) {
				return JSONUtils.newFailureResult("用户名或密码错误", ErrorCode.WRONG_NAME_OR_PASSWD, "");
			}
			// 设置登陆状态
			session.login(user);
			logger.debug("user logined {}", user);
			return JSONUtils.newSuccessfulResult("登陆成功", packSuccess(user));
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}

	/**
	 * 注销
	 * {}
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "logout", level=PrivilegeLevel.LOGGED)
	public String logout() throws ServiceException {
		if (!session.isLogined()) { // 未登陆
			return JSONUtils.newFailureResult("您未登陆", ErrorCode.AUTH, "");
		}
		
		session.logout();
		logger.debug("user logout User[id={}, name={}]", session.getUser().getId(), session.getUser().getUsername());
		return JSONUtils.newSuccessfulResult("注销成功");
	}
	
	
	private JSONObject packSuccess(User user) {
		JSONObject role = new JSONObject();
		role.put("roleId", user.getRole().getId());
		role.put("description", user.getRole().getDescription());
		
		JSONObject result = new JSONObject();
		result.put("userId", user.getId());
		result.put("selfShield", user.isSelfForbid());
		result.put("adminShield", user.isAdminForbid());
		result.put("role", role);
		return result;
	}
}
