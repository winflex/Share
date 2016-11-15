package cc.lixiaohui.share.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import cc.lixiaohui.share.protocol.util.builder.MessageBuilder;

/**
 * 消息基类
 * 
 * @author lixiaohui
 * @date 2016年11月7日 下午10:03:45
 */
public class Message implements Serializable{
	
	private static final long serialVersionUID = 4308692138692271553L;
	
	private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
	
	/**
	 * 消息唯一标识
	 */
	protected long id;
	
	/**
	 * 附加属性
	 */
	protected Map<String, Serializable> properties = new HashMap<String, Serializable>();
	
	/**
	 * 必须有无参构造方法
	 */
	public Message(){}
	
	public Message(MessageBuilder builder) {
		this.id = ID_GENERATOR.getAndIncrement();
		this.properties = builder.properties();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public static MessageBuilder builder() {
		return new MessageBuilder();
	}
	
	public static void main(String[] args) throws Exception {
		Message m1 = Message.class.newInstance();
		Message m2 = Message.class.newInstance();
		System.out.println(m1.getId());
		System.out.println(m2.getId());
	}
	
}
