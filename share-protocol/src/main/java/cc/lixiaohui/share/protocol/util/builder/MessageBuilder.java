package cc.lixiaohui.share.protocol.util.builder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.util.IBuilder;

public class MessageBuilder implements IBuilder<Message>{
	
	protected long id;
	
	protected Map<String, Serializable> properties = new HashMap<String, Serializable>();

	
	public long id() {
		return id;
	}

	public MessageBuilder id(long id) {
		this.id = id;
		return this;
	}

	public MessageBuilder property(String key, Serializable value) {
		properties.put(key, value);
		return this;
	}

	public Serializable property(String key) {
		return properties.get(key);
	}

	public Map<String, Serializable> properties() {
		return properties;
	}

	public MessageBuilder properties(Map<String, Serializable> properties) {
		this.properties = properties;
		return this;
	}

	/* 
	 * @see cc.lixiaohui.share.util.IBuilder#build()
	 */
	@Override
	public Message build() {
		return new Message(this);
	}
}