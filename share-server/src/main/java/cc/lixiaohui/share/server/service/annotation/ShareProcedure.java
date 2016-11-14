package cc.lixiaohui.share.server.service.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识服务过程
 * @author lixiaohui
 * @date 2016年11月13日 下午11:48:48
 */
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShareProcedure {
	
	/**
	 * 过程名
	 * @return
	 */
	public String name();
	
}
