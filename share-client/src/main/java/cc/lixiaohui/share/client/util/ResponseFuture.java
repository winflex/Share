package cc.lixiaohui.share.client.util;

import io.netty.channel.Channel;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.future.AbstractFuture;

/**
 * @author lixiaohui
 * @date 2016年11月16日 下午2:55:38
 */
public class ResponseFuture extends AbstractFuture<Message> {

	private Message originMessage;
	
	private Channel originChannel;
	
	private long sendTime;
	
	public ResponseFuture(Message message, Channel ctx) {
		this.originMessage = message;
		this.originChannel = ctx;
		sendTime = TimeUtils.currentTimeMillis();
	}
	
	public void responsed(Message message) {
		setSuccess(message);
	}
	
	public void failed(Throwable cause) {
		setFailure(cause);
	}

	public void timeout() {
		setFailure(null);
	}
	
	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public Message getOriginMessage() {
		return originMessage;
	}

	public void setOriginMessage(Message message) {
		this.originMessage = message;
	}

	public Channel getOriginChannel() {
		return originChannel;
	}

	public void setOriginChannel(Channel channel) {
		this.originChannel = channel;
	}

}
