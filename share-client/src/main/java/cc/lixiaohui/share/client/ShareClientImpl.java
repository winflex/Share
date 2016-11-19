package cc.lixiaohui.share.client;

import java.util.concurrent.TimeUnit;

import cc.lixiaohui.share.client.util.ClientException;
import cc.lixiaohui.share.client.util.Param;
import cc.lixiaohui.share.client.util.Proxy;
import cc.lixiaohui.share.client.util.ResponseFuture;
import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.protocol.ResponseMessage;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.Objects;
import cc.lixiaohui.share.util.future.IFuture;
import cc.lixiaohui.share.util.future.IFutureListener;

/**
 * 客户端实现
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午4:42:25
 */
public class ShareClientImpl extends AbstractShareClient {
	
	protected ShareClientImpl(String host, int port) {
		super(host, port);
	}

	@Override
	public void send(int userId, String message) throws ClientException {
		send(userId, message, null);
	}

	@Override
	public void send(int userId, String message, final ISentMessageListener listener) throws ClientException {
		message = Objects.requireNonNull(message, "message"); 
		final CSCRequestMessage msg = CSCRequestMessage.builder().toUserId(userId).text(message).build();
		ResponseFuture future = new ResponseFuture(msg, channel);
		putFuture(msg.getId(), future);
		// do write
		writeMessage(channel, msg);
		if (listener != null) {
			future.addListener(new IFutureListener<Message>() {
				@Override
				public void operationCompleted(IFuture<Message> future) throws Exception {
					if (future.isSuccess()) {
						// 这里也可能是超时, 因为服务端有可能返回超时信息
						listener.onResponsed(((ResponseMessage)future.get()).getRespJson());
					} else {
						listener.onResponsed(JSONUtils.newFailureResult("请求超时", ErrorCode.TIMEOUT, ""));
					}
				}
				
			});
		}
		// remove future
		future.addListener(new IFutureListener<Message>() {

			@Override
			public void operationCompleted(IFuture<Message> future) throws Exception {
				removeFuture(msg.getId());
			}
		});
		
		// TODO 考虑消息超时的情况, 和同步发送不同, 由于是异步的调用, 假如消息超时, 那么回调可能很久之后依然不会被调用, 因此需要一个定时任务来检查是否超时
	}
	
	/**
	 * 同步发送Message, 超时时间由{@link HandShakeRequestMessage#getRequestTimeout()} 指定
	 * @param message 要发送的消息
	 * @return 返回的json
	 * @throws ClientException if any exception
	 */
	String syncSend(final Message message) throws ClientException {
		checkHandshake();
		ResponseFuture future = new ResponseFuture(message, channel);
		putFuture(message.getId(), future);
		// do write
		writeMessage(channel, message);
		
		try {
			// wait for response for a specified time limit
			future.await(handshakeMessage.getRequestTimeout(), TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ClientException(e);
		} finally {
			// 保证能删除future
			removeFuture(message.getId());
		}
		
		if (future.isSuccess()) {
			ResponseMessage resp = (ResponseMessage) future.getNow();
			return resp.getRespJson();
		} else {
			return JSONUtils.newFailureResult("请求超时", ErrorCode.TIMEOUT, "");
		}
	}
	
	// -------------- follows are proxy methods -----------------
	
	@Proxy(service="SessionService", procedure="register", params=
		{@Param(index = 0, name = "username"), @Param(index = 1, name = "password")})
	@Override
	public String register(String username, String password) throws ClientException {
		return null;
	}

	@Proxy(service="SessionService", procedure="login", params=
		{@Param(index = 0, name = "username"), @Param(index = 1, name = "password")})
	@Override
	public String login(String username, String password) throws ClientException {
		return null;
	}

	@Proxy(service="SessionService", procedure="logout")
	@Override
	public String logout() throws ClientException {
		return null;
	}

	@Proxy(service="UserService", procedure="getUser", params={@Param(index = 0, name = "userId")})
	@Override
	public String getUser(int userId) throws ClientException {
		return null;
	}

	@Proxy(service="UserService", procedure="updateUser", params={
			@Param(index = 0, name = "password"),
			@Param(index = 1, name = "sex"),
			@Param(index = 2, name = "signature"),
			@Param(index = 3, name = "headImageId")})
	@Override
	public String updateUser(String password, String sex, String signature, int headImageId) throws ClientException {
		return null;
	}

	@Proxy(service="UserService", procedure="shield", params={
			@Param(index = 0, name = "userId")})
	@Override
	public String shield(int userId) throws ClientException {
		return null;
	}


	@Proxy(service="ShareService", procedure="getShare", params={
			@Param(index = 0, name = "shareId")})
	@Override
	public String getShare(int shareId) throws ClientException {
		return null;
	}

	@Proxy(service="ShareService", procedure="publishShare", params={
			@Param(index = 0, name = "content"),
			@Param(index = 1, name = "pictureIds")})
	@Override
	public String publishShare(String content, int[] picturesIds) throws ClientException {
		return null;
	}

	@Proxy(service="CommentService", procedure="getCommentOfShare", params={
			@Param(index = 0, name = "shareId"),
			@Param(index = 1, name = "start"),
			@Param(index = 2, name = "limit")})
	@Override
	public String getComments(int shareId, int start, int limit) throws ClientException {
		return null;
	}


	@Proxy(service="CommentService", procedure="deleteCommentOfUser", params={
			@Param(index = 0, name = "commentId")})
	@Override
	public String deleteComment(int commentId) throws ClientException {
		return null;
	}


	@Proxy(service="UserService", procedure="getFriends", params={
			@Param(index = 0, name = "userId"),
			@Param(index = 1, name = "start"),
			@Param(index = 2, name = "limit")})
	@Override
	public String getFriends(int userId, int start, int limit) throws ClientException {
		return null;
	}

	@Proxy(service="UserService", procedure="deleteFriend", params={
			@Param(index = 0, name = "friendId")})
	@Override
	public String deleteFriend(int friendId) throws ClientException {
		return null;
	}

	@Proxy(service="PictureService", procedure="uploadPicture", params={
			@Param(index = 0, name = "suffix"),
			@Param(index = 1, name = "bytes")})
	@Override
	public String uploadPicture(String suffix, byte[] bytes) throws ClientException {
		return null;
	}

	@Proxy(service="PictureService", procedure="getPictures", params={
			@Param(index = 0, name = "ignoreIfNotExist"),
			@Param(index = 1, name = "pictureIds")})
	@Override
	public String getPictures(boolean ignoreIfNotExist, int[] pictureIds) throws ClientException {
		return null;
	}

	@Proxy(service="PictureService", procedure="deletePicture", params={
			@Param(index = 0, name = "pictureId")})
	@Override
	public String deletePicture(int pictureId) throws ClientException {
		return null;
	}

	@Proxy(service="CollectionService", procedure="getUserCollection", params={
			@Param(index = 0, name = "start"),
			@Param(index = 1, name = "limit")})
	@Override
	public String getUserCollection(int start, int limit) throws ClientException {
		return null;
	}

	@Proxy(service="CollectionService", procedure="getShareCollection", params={
			@Param(index = 0, name = "start"),
			@Param(index = 1, name = "limit")})
	@Override
	public String getShareCollection(int start, int limit) throws ClientException {
		return null;
	}

	@Proxy(service="CollectionService", procedure="unCollectUser", params={
			@Param(index = 0, name = "userId")})
	@Override
	public String unCollectUser(int userId) throws ClientException {
		return null;
	}

	@Proxy(service="CollectionService", procedure="unCollectShare", params={
			@Param(index = 0, name = "shareId")})
	@Override
	public String unCollectShare(int shareId) throws ClientException {
		return null;
	}

	@Proxy(service="SortingService", procedure="getSortings")
	@Override
	public String getSortings() throws ClientException {
		return null;
	}

	@Proxy(service="ForbidenWordService", procedure="getForbidenWords", params={
			@Param(index = 0, name = "start"),
			@Param(index = 1, name = "limit")})
	@Override
	public String getForbidenWords(int start, int limit) throws ClientException {
		return null;
	}

	@Proxy(service="ForbidenWordService", procedure="getDeletedForbidenWords", params={
			@Param(index = 0, name = "start"),
			@Param(index = 1, name = "limit")})
	@Override
	public String getDeletedForbidenWords(int start, int limit) throws ClientException {
		return null;
	}

	@Proxy(service="ForbidenWordService", procedure="deleteForbidendWord", params={
			@Param(index = 0, name = "wordId")})
	@Override
	public String deleteForbidendWord(int wordId) throws ClientException {
		return null;
	}

	@Proxy(service="ForbidenWordService", procedure="recoverForbidendWord", params={
			@Param(index = 0, name = "wordId")})
	@Override
	public String recoverForbidendWord(int wordId) throws ClientException {
		return null;
	}

	@Proxy(service="ShareService", procedure="getShares", params={
			@Param(index = 0, name = "keyword"),
			@Param(index = 1, name = "orderColumn"),
			@Param(index = 2, name = "orderType"),
			@Param(index = 3, name = "start"),
			@Param(index = 4, name = "limit"),
			@Param(index = 5, name = "deleted")
	})
	@Override
	public String getShares(String keyword, int orderColumn, int orderType, int start, int limit, boolean deleted) throws ClientException {
		return null;
	}

	@Proxy(service="ShareService", procedure="deleteShare", params={
			@Param(index = 0, name = "shareId"),
			@Param(index = 1, name = "physically")
	})
	@Override
	public String deleteShare(int shareId, boolean physically) throws ClientException {
		return null;
	}
	
	@Proxy(service="CommentService", procedure="publishComment", params={
			@Param(index = 0, name = "shareId"),
			@Param(index = 1, name = "toUserId"),
			@Param(index = 2, name = "content")
	})
	@Override
	public String publishComment(int shareId, int toUserId, String content) throws ClientException {
		return null;
	}

	
	@Proxy(service="UserService", procedure="searchUser", params={
			@Param(index = 0, name = "keyword"),
			@Param(index = 1, name = "start"),
			@Param(index = 2, name = "limit")
	})
	@Override
	public String searchUser(String keyword, int start, int limit) throws ClientException {
		return null;
	}

	@Proxy(service="UserService", procedure="searchUser", params={
			@Param(index = 0, name = "targetUserId")
	})
	@Override
	public String addFriend(int targetUserId) throws ClientException {
		return null;
	}

}
