package cc.lixiaohui.share.protocol.codec.serialize;

import java.io.IOException;

/**
 * 标准序列化器
 * 
 * @author lixiaohui
 * @date 2016年9月22日 下午1:50:59
 * 
 */
public interface ISerializer {
	
	<T> byte[] serialize(T obj) throws IOException;
	
	<T> T deserialize(byte[] stream) throws IOException;
	
}