package cc.lixiaohui.share.server.service;

import java.util.Map;

import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.server.Session;
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
	 * <pre>
	 * String username, String password
	 * {
	 *  	"userId":123     # 注册成功的用户ID
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
			User user = new User(username, password);
			int n = dao.add(user);
			if (n > 0) {
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
	 * 
	 * @param username String, !nullable
	 * @param password String, !nullable
	 * @return
	 * <pre>
	 * String username, String password
	 * {
	 *  "userId":123     # 注册成功的用户ID
	 *  "selfShield":false,			# 是否子屏蔽
	 *  "adminShield":false			# 是否被管理员屏蔽
	 * }
	 * </pre>
	 * @throws ServiceException
	 */
	@Procedure(name = "login")
	public String login() throws ServiceException {
		
		if (session.isLogined()) { // 已登陆
			return JSONUtils.newSuccessfulResult("您已经登陆", packSuccess(session.getUserId(), session.isSelfShield(), session.isAdminShield()));
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
			session.login(user.getId(), user.getUsername(), user.isSelfForbid(), user.isAdminForbid());
			logger.debug("user logined {}", user);
			return JSONUtils.newSuccessfulResult("登陆成功", packSuccess(user));
		} catch (Exception e) {
			logger.error("{}", e);
			return JSONUtils.newFailureResult(e.getMessage(), ErrorCode.wrap(e), e);
		}
	}

	/**
	 * {}
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "logout")
	public String logout() throws ServiceException {
		if (!session.isLogined()) { // 未登陆
			return JSONUtils.newFailureResult("您未登陆", ErrorCode.AUTH, "");
		}
		
		session.logout();
		logger.debug("user logout User[id={}, name={}]", session.getUserId(), session.getUsername());
		return JSONUtils.newSuccessfulResult("注销成功");
	}
	
	
	private JSONObject packSuccess(User user) {
		return packSuccess(user.getId(), user.isSelfForbid(), user.isAdminForbid());
	}

	private JSONObject packSuccess(int userId, boolean selfShield, boolean adminShield) {
		JSONObject result = new JSONObject();
		result.put("userId", userId);
		result.put("selfShield", selfShield);
		result.put("adminShield", adminShield);
		return result;
	}
	
}
