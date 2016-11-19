package cc.lixiaohui.share.protocol.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import java.io.IOException;

import org.junit.Test;

import cc.lixiaohui.share.protocol.HandShakeRequestMessage;
import cc.lixiaohui.share.protocol.codec.MessageDecoder;
import cc.lixiaohui.share.protocol.codec.MessageEncoder;
import cc.lixiaohui.share.protocol.codec.serialize.factory.HessianSerializeFactory;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;

/**
 * @author lixiaohui
 * @date 2016年11月17日 上午10:08:43
 */
public class PipelineTest {
	
	@SuppressWarnings("unused")
	@Test
	public void test() throws IOException {
		final ISerializeFactory factory = new HessianSerializeFactory();
		EmbeddedChannel channel = new EmbeddedChannel(new MessageDecoder(factory), new MessageEncoder(factory));
		
		HandShakeRequestMessage message = HandShakeRequestMessage.builder().reconnectInterval(1).reconnectTimes(1).requestTimeout(1).heartbeatInterval(1).build();
		
		channel.writeOutbound(message);
		ByteBuf buf =  (ByteBuf) channel.readOutbound();
		
		channel.writeInbound(buf);
		Object o = channel.readInbound();
		System.out.println();
	}
	
}
