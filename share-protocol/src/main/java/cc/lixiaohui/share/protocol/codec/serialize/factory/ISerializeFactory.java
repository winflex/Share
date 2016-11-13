package cc.lixiaohui.share.protocol.codec.serialize.factory;

import cc.lixiaohui.share.protocol.codec.serialize.ISerializer;

/**
 * @author lixiaohui
 * @date 2016年11月7日 下午10:16:36
 */
public interface ISerializeFactory {
	
	ISerializer get();
	
}
