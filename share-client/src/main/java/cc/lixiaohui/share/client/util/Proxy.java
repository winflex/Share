package cc.lixiaohui.share.client.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cc.lixiaohui.share.protocol.CSRequestMessage;

/**
 * 标识需要代理的方法
 * @author lixiaohui
 * @date 2016年11月16日 下午4:03:26
 */
@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Proxy {
	
	public Class<CSRequestMessage> message() default CSRequestMessage.class;
	
	public String service();
	
	public String procedure();
	
	/**
	 * 参数
	 * @return
	 */
	public Param[] params() default {};
	
	public String implMethod() default "syncSend";
	
}
