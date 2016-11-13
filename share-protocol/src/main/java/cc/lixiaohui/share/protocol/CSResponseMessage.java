package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.CSResponseMessageBuilder;

/**
 * 
 * @author lixiaohui
 * @date 2016年11月7日 下午10:23:07
 */
public class CSResponseMessage extends ResponseMessage {

	private static final long serialVersionUID = -5387210902045864975L;

	public CSResponseMessage(CSResponseMessageBuilder builder) {
		super(builder);
	}

	public static CSResponseMessageBuilder builder() {
		return new CSResponseMessageBuilder();
	}
}
