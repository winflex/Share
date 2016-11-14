package cc.lixiaohui.share.client;

import java.io.InputStream;

/**
 * 客户端最终实现
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午4:42:25
 */
public class ShareClientImpl extends AbstractShareClient {

	ShareClientImpl(IConfiguration config) {
		super(config);
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#register(java.lang.String, java.lang.String)
	 */
	@Override
	public String register(String username, String password) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#login(java.lang.String, java.lang.String)
	 */
	@Override
	public String login(String username, String password) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#logout()
	 */
	@Override
	public String logout() throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getUser(int)
	 */
	@Override
	public String getUser(int userId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#updateUser(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public String updateUser(String password, String sex, String signature, int headImageId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#shield(int)
	 */
	@Override
	public String shield(int userId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getShare(int)
	 */
	@Override
	public String getShare(int shareId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#deleteShare(int)
	 */
	@Override
	public String deleteShare(int shareId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#publishShare(java.lang.String, int[])
	 */
	@Override
	public String publishShare(String content, int[] picturesIds) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getCommentOfShare(int, int, int)
	 */
	@Override
	public String getCommentOfShare(int shareId, int start, int limit) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getCommentOfUser(int, int)
	 */
	@Override
	public String getCommentOfUser(int start, int limit) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#deleteCommentOfUser(int)
	 */
	@Override
	public String deleteCommentOfUser(int commentId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#commentUser(int, java.lang.String, int)
	 */
	@Override
	public String commentUser(int shareId, String content, int toUserId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#commentShare(int, java.lang.String)
	 */
	@Override
	public String commentShare(int shareId, String content) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getFriends(int, int, int)
	 */
	@Override
	public String getFriends(int userId, int start, int limit) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#deleteFriend(int)
	 */
	@Override
	public String deleteFriend(int friendId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#uploadPicture(java.lang.String, byte[])
	 */
	@Override
	public String uploadPicture(String suffix, byte[] bytes) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#uploadPicture(java.io.InputStream)
	 */
	@Override
	public String uploadPicture(InputStream in) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getPictures(boolean, int[])
	 */
	@Override
	public String getPictures(boolean ignoreIfNotExist, int[] pictureIds) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#deletePicture(int)
	 */
	@Override
	public String deletePicture(int pictureId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getUserCollection(int, int)
	 */
	@Override
	public String getUserCollection(int start, int limit) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getShareCollection(int, int)
	 */
	@Override
	public String getShareCollection(int start, int limit) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#unCollectUser(int)
	 */
	@Override
	public String unCollectUser(int userId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#unCollectionShare(int)
	 */
	@Override
	public String unCollectionShare(int shareId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getSortings()
	 */
	@Override
	public String getSortings() throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getForbidenWords(int, int)
	 */
	@Override
	public String getForbidenWords(int start, int limit) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getDeletedForbidenWords(int, int)
	 */
	@Override
	public String getDeletedForbidenWords(int start, int limit) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#deleteForbidendWord(int)
	 */
	@Override
	public String deleteForbidendWord(int wordId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#recoverForbidendWord(int)
	 */
	@Override
	public String recoverForbidendWord(int wordId) throws ShareClientException {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IImmediateShareClient#send(int, java.lang.String)
	 */
	@Override
	public void send(int userId, String message) throws ShareClientException {
	}

	/* 
	 * @see cc.lixiaohui.share.client.IImmediateShareClient#send(int, java.lang.String, cc.lixiaohui.share.client.IImmediateShareClient.ISentMessageListener)
	 */
	@Override
	public void send(int userId, String message, ISentMessageListener listener) throws ShareClientException {
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#getShares(java.lang.String, int, int, int, int)
	 */
	@Override
	public String getShares(String keyword, int orderColumn, int orderType, int start, int limit) throws ShareClientException {
		return null;
	}
	
	
	
}
