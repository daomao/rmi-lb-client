package calculator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhongfeng
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RmiServiceExplorer {

	/**
	 * rmi service interface
	 * @return
	 */
	public Class<?> serviceInterface() default Object.class;

	/**
	 * service name
	 * @return
	 */
	public String serviceName() default "";

	/**
	 * registry id
	 * @return
	 */
	public String registryId() default "";

}
