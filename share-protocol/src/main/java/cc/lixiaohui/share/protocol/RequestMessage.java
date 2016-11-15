package cc.lixiaohui.share.protocol;

import cc.lixiaohui.share.protocol.util.builder.MessageBuilder;

/**
 * 请求消息基类
 * @author lixiaohui
 * @date 2016年11月7日 下午10:05:40
 */
public class RequestMessage extends Message {

	private static final long serialVersionUID = 3052323457401526369L;
	
	public RequestMessage() {}
	
	public RequestMessage(MessageBuilder builder) {
		super(builder);
	}

}
