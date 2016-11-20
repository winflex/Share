package cc.lixiaohui.share.server.service;

import java.util.List;
import java.util.Map;

import cc.lixiaohui.share.model.bean.Comment;
import cc.lixiaohui.share.model.bean.Share;
import cc.lixiaohui.share.model.bean.User;
import cc.lixiaohui.share.model.dao.CommentDao;
import cc.lixiaohui.share.model.dao.ShareDao;
import cc.lixiaohui.share.model.dao.UserDao;
import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.service.util.PrivilegeLevel;
import cc.lixiaohui.share.server.service.util.ServiceException;
import cc.lixiaohui.share.server.service.util.annotation.Procedure;
import cc.lixiaohui.share.server.service.util.annotation.Service;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lixiaohui
 * @date 2016年11月11日 下午10:00:25
 */
@Service(name = "CommentService")
public class CommentService extends AbstractService {

	public CommentService(Session session, Map<String, Object> parameters) {
		super(session, parameters);
	}

	/**
	 * int shareId, int start, int limit
	 * @param share int, !nullable
	 * @param start int, nullable
	 * @param limit int, nullable
	 * <pre>
	 * {
	 *   "count":2
	 *   "comments":[
	 *    {
	 *     "id":11,                   # 评论ID
	 *     "shareId":32               # 所属分享的ID
	 *     "content":"good!!",        # 评论的内容
	 *     "commentTime":1465565576,  # 评论时间
	 *     "fromUserId":1             # 评论者ID
	 *     "fromUsername":"lixiaohui" # 评论者用户名
	 *     "toUserId":2               # 被评论的用户ID
	 *     "toUsername":"zhangsan"    # 被评论的用户ID
	 *    },
	 *    {
	 *     "id":11,                   # 评论ID
	 *     "shareId":32               # 所属分享的ID
	 *     "content":"good!!",        # 评论的内容
	 *     "commentTime":1465565576,  # 评论时间
	 *     "fromUserId":1             # 评论者ID
	 *     "fromUsername":"lixiaohui" # 评论者用户名
	 *     "toUserId":2               # 被评论的用户ID
	 *     "toUsername":"zhangsan"    # 被评论的用户ID
	 *    }
	 *   ]
	 * }
	 * </pre>
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "getComments")
	public String getComments() throws ServiceException {
		try {
			int shareId = getIntParameter("shareId");
			int start = getIntParameter("start", DEFAULT_START);
			int limit = getIntParameter("limit", DEFAULT_LIMIT);
			
			CommentDao cdao = daofactory.getDao(CommentDao.class);
			List<Comment> comments = cdao.getByShareId(shareId, start, limit);
			return JSONUtils.newSuccessfulResult("获取评论成功", packComment(shareId, comments));
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
		
	}
	
	/**
	 * int commentId
	 * {}
	 * @return
	 * @throws ServiceException
	 */
	@Procedure(name = "deleteComment", level=PrivilegeLevel.LOGGED)
	public String deleteComment() throws ServiceException {
		if (!session.isLogined()) {
			return JSONUtils.newFailureResult("您未登录", ErrorCode.AUTH, "");
		}
		
		try {
			int commentId = getIntParameter("commentId");
			CommentDao dao = daofactory.getDao(CommentDao.class);
			Comment comment = dao.getById(commentId);
			if (comment.getFromUser().getId() != session.getUser().getId()) {
				return JSONUtils.newFailureResult("无法删除他人的评论", ErrorCode.AUTH, "");
			}
			
			// TODO 物理删除
			if (dao.delete(commentId) > 0) {
				return JSONUtils.newSuccessfulResult("删除评论成功");
			} else {
				return JSONUtils.newFailureResult("删除评论失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
	}
	
	
	/**
	 * 发布评论
	 * @param shareId int, !nullable
	 * @param toUserId int, !nullable
	 * @param content String, !nullable
	 * {
	 *   "commentId":1
	 * }
	 * @return 
	 */
	@Procedure(name = "publishComment", level=PrivilegeLevel.LOGGED)
	public String publishComment() throws ServiceException{
		if (!session.isLogined()) {
			return JSONUtils.newFailureResult("您未登陆", ErrorCode.AUTH, "");
		}
		try {
			int shareId = getIntParameter("shareId");
			int toUserId = getIntParameter("toUserId");
			String content = getStringParameter("content");
			
			CommentDao commentDao = daofactory.getDao(CommentDao.class);
			ShareDao shareDao = daofactory.getDao(ShareDao.class);
			UserDao userDao = daofactory.getDao(UserDao.class);
			
			Share share = shareDao.getById(shareId);
			User fromUser = userDao.getById(session.getUser().getId());
			User toUser = session.getUser().getId() == toUserId ? fromUser : userDao.getById(toUserId);
			
			if (share == null) {
				return JSONUtils.newFailureResult("分享不存在", ErrorCode.RESOURCE_NOT_FOUND, "");
			}
			
			if (toUser == null) {
				return JSONUtils.newFailureResult("您所回复的用户不存在", ErrorCode.RESOURCE_NOT_FOUND, "");
			}
			
			Comment comment = newComment(content, share, fromUser, toUser);
			if (commentDao.add(comment) > 0) {
				// TODO 推送(只推送给分享的发布者)
				try {
					PushMessage message = PushMessage.builder().type(PushMessage.Type.COMMENT)
							.pushData(packPushComment(comment).toJSONString()).build();
					session.getSessionManager().pushTo(share.getPublisher().getId(), message);
				} catch (Throwable t) {
					logger.error("{}", t);
				}
				return JSONUtils.newSuccessfulResult("评论成功", packComment(comment));
			} else {
				return JSONUtils.newFailureResult("评论失败", ErrorCode.UNKOWN, "");
			}
		} catch (Throwable t) {
			logger.error("{}", t);
			return JSONUtils.newFailureResult(t.getMessage(), ErrorCode.wrap(t), t);
		}
	}

	private JSONObject packPushComment(Comment comment) {
		JSONObject pushResult = new JSONObject();
		pushResult.put("id", comment.getId());
		pushResult.put("shareId", comment.getShare().getId());
		pushResult.put("content", comment.getContent());
		pushResult.put("commentTime", comment.getCommentTime());
		pushResult.put("fromUserId", comment.getFromUser().getId());
		pushResult.put("fromUsername", comment.getFromUser().getUsername());
		pushResult.put("toUserId", comment.getToUser().getId());
		pushResult.put("toUsername", comment.getToUser().getUsername());
		return pushResult;
	}

	private Comment newComment(String content, Share share, User fromUser, User toUser) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setFromUser(fromUser);
		comment.setToUser(toUser);
		comment.setShare(share);
		return comment;
	}
	
	private JSONObject packComment(int shareId, List<Comment> comments) {
		JSONArray commentArray = new JSONArray();
		for (Comment c : comments) {
			JSONObject item = new JSONObject();
			item.put("id", c.getId());
			item.put("shareId", shareId);
			item.put("content", c.getContent());
			item.put("commentTime", c.getCommentTime());
			item.put("fromUserId", c.getFromUser().getId());
			item.put("fromUsername", c.getFromUser().getUsername());
			item.put("toUserId", c.getToUser().getId());
			item.put("toUsername", c.getToUser().getUsername());
			commentArray.add(item);
		}
		
		JSONObject result = new JSONObject();
		result.put("count", comments.size());
		result.put("comments", commentArray);
		return result;
	}

	private JSONObject packComment(Comment comment) {
		JSONObject result = new JSONObject();
		result.put("commentId", comment.getId());
		return result;
	}
}
