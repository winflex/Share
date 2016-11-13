package cc.lixiaohui.share.protocol.codec.serialize.factory;

import cc.lixiaohui.share.protocol.codec.serialize.HessianSerializer;
import cc.lixiaohui.share.protocol.codec.serialize.ISerializer;

/**
 * 默认采用 Hessian 序列化, 每个工厂只生产一个 {@link ISerializer}
 * @author lixiaohui
 * @date 2016年11月7日 下午10:18:00
 */
public class HessianSerializeFactory implements ISerializeFactory {

	private final ISerializer SERIALIZER = new HessianSerializer();
	
	@Override
	public ISerializer get() {
		return SERIALIZER;
	}

}
