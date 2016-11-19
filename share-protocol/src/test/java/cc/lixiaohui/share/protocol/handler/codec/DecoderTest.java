package cc.lixiaohui.share.protocol.handler.codec;

import java.io.IOException;

import org.junit.Test;

import cc.lixiaohui.share.protocol.PushMessage;
import cc.lixiaohui.share.protocol.codec.serialize.factory.HessianSerializeFactory;
import cc.lixiaohui.share.protocol.codec.serialize.factory.ISerializeFactory;

/**
 * @author lixiaohui
 * @date 2016年11月17日 上午10:00:07
 */
public class DecoderTest {
	
	@Test
	public void test() throws IOException {
		ISerializeFactory factory = new HessianSerializeFactory();
		PushMessage message = PushMessage.builder().pushData("dsa").type(PushMessage.Type.FRI_REQ).build();
		byte[] bytes = factory.get().serialize(message);
		PushMessage m = factory.get().deserialize(bytes);
		System.out.println(m);
	}
	
}
