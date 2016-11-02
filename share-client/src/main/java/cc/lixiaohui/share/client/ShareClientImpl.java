package cc.lixiaohui.share.client;

/**
 * 客户端最终实现
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午4:42:25
 */
class ShareClientImpl extends AbstractShareClient {

	ShareClientImpl(Configuration config) {
		super(config);
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String username, String password) {
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#isLogined()
	 */
	@Override
	public boolean isLogined() {
		return false;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#loginedName()
	 */
	@Override
	public String loginedName() {
		return null;
	}

	/* 
	 * @see cc.lixiaohui.share.client.IShareClient#logout()
	 */
	@Override
	public void logout() {
	}
	
	
	
}
