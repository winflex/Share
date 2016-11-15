package cc.lixiaohui.share.server.service.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cc.lixiaohui.share.server.service.util.PrivilegeLevel;

/**
 * 标识服务过程
 * @author lixiaohui
 * @date 2016年11月13日 下午11:48:48
 */
@Target(value = { ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Procedure {
	
	/**
	 * 过程名
	 * @return
	 */
	public String name();
	
	/**
	 * 要求的权限等级, 默认为{@link PrivilegeLevel#HANDSHAKED}
	 * @return
	 */
	public PrivilegeLevel level() default PrivilegeLevel.HANDSHAKED;
	
}
