package cc.lixiaohui.share.protocol.codec.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * 基于Hessian的序列化实现
 * 
 * @author lixiaohui
 * @date 2016年11月7日 下午10:12:56
 */
public class HessianSerializer implements ISerializer {

	@Override
	public <T> byte[] serialize(T obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		HessianOutput out = new HessianOutput(baos);
		out.writeObject(obj);
		out.flush();
		return baos.toByteArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] stream) throws IOException {
		HessianInput in = new HessianInput(new ByteArrayInputStream(stream));
		return (T) in.readObject();
	}

}
