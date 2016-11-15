package cc.lixiaohui.share.server.service.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识服务类
 * @author lixiaohui
 * @date 2016年11月13日 下午11:41:54
 */
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
	
	/**
	 * 服务名
	 * @return
	 */
	public String name();
	
}
