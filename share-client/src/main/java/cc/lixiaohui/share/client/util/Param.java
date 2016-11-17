package cc.lixiaohui.share.client.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lixiaohui
 * @date 2016年11月16日 下午4:39:38
 */
@Target(value=ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
	public int index();
	
	public String name();
}
