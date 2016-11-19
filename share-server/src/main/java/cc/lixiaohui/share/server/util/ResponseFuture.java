package cc.lixiaohui.share.server.util;

import io.netty.channel.Channel;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.future.AbstractFuture;

/**
 * @author lixiaohui
 * @date 2016年11月17日 下午2:47:54
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
	
	public Message getOriginMessage() {
		return originMessage;
	}

	public void setOriginMessage(Message originMessage) {
		this.originMessage = originMessage;
	}

	public Channel getOriginChannel() {
		return originChannel;
	}

	public void setOriginChannel(Channel originChannel) {
		this.originChannel = originChannel;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
}
