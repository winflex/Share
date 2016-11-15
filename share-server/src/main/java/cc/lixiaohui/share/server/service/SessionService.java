package cc.lixiaohui.share.server.service;

import java.util.Map;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;

/**
 * 处理登录注册注销
 * @author lixiaohui
 * @date 2016年11月11日 下午10:30:38
 */
@Service(name = "SessionService")
public class SessionService extends AbstractService {

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
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "register")
	public String register() throws ServiceException {
		return null;
	}
	
	/**
	 * <pre>
	 * String username, String password
	 * {
	 *  "userId":123     # 注册成功的用户ID
	 * }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "login")
	public String login() throws ServiceException {
		return null;
	}
	
	/**
	 * {}
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "logout")
	public String logout() throws ServiceException {
		return null;
	}
}
