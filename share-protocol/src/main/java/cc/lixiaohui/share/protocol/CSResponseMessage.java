package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.CSResponseBuilder;

/**
 * 
 * @author lixiaohui
 * @date 2016年11月7日 下午10:23:07
 */
public class CSResponseMessage extends ResponseMessage {

	private static final long serialVersionUID = -5387210902045864975L;

	public CSResponseMessage() {}
	
	public CSResponseMessage(CSResponseBuilder builder) {
		super(builder);
	}

	public static CSResponseBuilder builder() {
		return new CSResponseBuilder();
	}
	
	@Override
	public String toString() {
		return String.format("CSResponseMessage[id=%d, corrId=%d, respJson=%s]", id, correlationId, respJson);
	}
}
