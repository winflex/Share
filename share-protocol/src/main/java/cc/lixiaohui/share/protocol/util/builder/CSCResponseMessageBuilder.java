package cc.lixiaohui.share.protocol.util.builder;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午10:18:51
 */
public class CSCResponseMessageBuilder extends ResponseMessageBuilder {

	protected long responseTime;
	
	public long responseTime() {
		return responseTime;
	}
	
	public CSCResponseMessageBuilder responseTime(long responseTime) {
		this.responseTime = responseTime;
		return this;
	}
}
