package cc.lixiaohui.share.client;

import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;

/**
 * 抽象实现
 * 
 * @author lixiaohui
 * @date 2016年10月31日 下午3:59:56
 */
public abstract class AbstractShareClient extends AbstractLifeCycle implements IShareClient {

	private Configuration config;
	
	protected AbstractShareClient(Configuration config) {
		this.config = config;
	}
	
}
