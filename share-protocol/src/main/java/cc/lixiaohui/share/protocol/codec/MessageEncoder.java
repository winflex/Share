package cc.lixiaohui.share.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;

/**
 * 协议编码器
 * @author lixiaohui
 * @date 2016年11月7日 下午10:40:06
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

	private final ISerializeFactory factory;
	
	public MessageEncoder(ISerializeFactory factory) {
		this.factory = factory;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		byte[] bodyBytes = factory.get().serialize(msg);
		out.writeInt(bodyBytes.length);
		out.writeBytes(bodyBytes);
	}

}
