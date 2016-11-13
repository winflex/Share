package cc.lixiaohui.share.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;

/**
 * 解码协议包
 * 
 * @author lixiaohui
 * @date 2016年11月7日 下午10:26:17
 */
public class MessageDecoder extends ByteToMessageDecoder {

	private static final int LENGTH_FIELD_LEGNTH = 4;
	
	private final ISerializeFactory factory;
	
	public MessageDecoder(ISerializeFactory factory) {
		this.factory = factory;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() > LENGTH_FIELD_LEGNTH) {
			if (in.readableBytes() > in.getInt(0)) {
				// consume packet length
				in.readInt();
				
				byte[] bodyBytes = new byte[in.readableBytes()];
				Object message = factory.get().deserialize(bodyBytes);
				out.add(message);
			}
		}
	}

}
