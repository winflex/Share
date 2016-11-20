package cc.lixiaohui.share.server.service;

import java.util.Map;

import cc.lixiaohui.share.model.bean.Praise;
import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.dao.PraiseDao;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lixiaohui
 * @date 2016年11月20日 下午6:27:01
 */
@Service(name="PraiseService")
public class PraiseService extends AbstractService {

	public PraiseService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * 点赞
	 * @param shareId int, !nullable
	 * @return
	 * <pre>
	 * {}
	 * </pre>
	 * @throws ServiceException
	 */
	@Procedure(name="like", level=PrivilegeLevel.LOGGED)
	public String like() throws ServiceException{
		try {
			int shareId = getIntParameter("shareId");
			PraiseDao dao = daofactory.getDao(PraiseDao.class);
			ShareDao shareDao = daofactory.getDao(ShareDao.class);
			//UserDao userDao = daofactory.getDao(UserDao.class);
			Share share = shareDao.getById(shareId);
			if (share == null) {
				return JSONUtils.newFailureResult("分享不存在", ErrorCode.UNKOWN, "");
			}
			Praise praise = new Praise(session.getUser(), share);
			if (dao.add(praise) > 0) {
				// TODO 推送(只推送给分享的发布者), 推送是附加操作, 推送失败不应导致整个操作失败
				try {
					PushMessage message = PushMessage.builder().type(PushMessage.Type.LIKE)
							.pushData(packLikePush(praise).toJSONString()).build();
					session.getSessionManager().pushTo(share.getPublisher().getId(), message);
				} catch (Throwable t) {
					logger.error("{}", t);
				}
				return JSONUtils.newSuccessfulResult("点赞成功");
			} else {
				return JSONUtils.newFailureResult("点赞失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable cause) {
			return JSONUtils.newFailureResult(cause);
		}
	}
	
	/**
	 * 取消点赞
	 * @param praiseId int, !nullable
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name="unlike", level=PrivilegeLevel.LOGGED)
	public String unlike() throws ServiceException {
		try {
			int praiseId = getIntParameter("praiseId");
			PraiseDao dao = daofactory.getDao(PraiseDao.class);
			Praise praise = dao.getById(praiseId);
			if (praise.getUser().getId() == session.getUser().getId()) {
				if (dao.delete(praise) > 0) {
					// TODO 推送给分享发布者
					try {
						PushMessage message = PushMessage.builder().type(PushMessage.Type.UNLIKE)
								.pushData(packUnlikePush(praise).toJSONString()).build();
						session.getSessionManager().pushTo(praise.getShare().getPublisher().getId(), message);
					} catch (Throwable cause) {
						logger.error("{}", cause);
					}
					return JSONUtils.newSuccessfulResult("取消点赞成功");
				} else {
					return JSONUtils.newFailureResult("取消点赞失败", ErrorCode.UNKOWN, "");
				}
			} else {
				return JSONUtils.newFailureResult("无法取消他人的点赞", ErrorCode.AUTH, "");
			}
			
		} catch (Throwable cause) {
			logger.error("{}", cause);
			return JSONUtils.newFailureResult(cause);
		}
	}
	
	private JSONObject packUnlikePush(Praise praise) {
		return packLikePush(praise);
	}
	
	private JSONObject packLikePush(Praise praise) {
		JSONObject result = new JSONObject();
		result.put("id", praise.getId());
		result.put("shareId", praise.getShare().getId());
		result.put("praiseUserId", praise.getUser().getId());
		result.put("praiseUsername", praise.getUser().getUsername());
		result.put("praiseTime", praise.getPraiseTime());
		return result;
	}
}
