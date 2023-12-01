package live.lingting.component.redis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Hccake
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface Cached {

	/**
	 * redis 存储的Key名
	 */
	String key();

	/**
	 * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式. 可以返回字符串或者数组或者list
	 * <p>
	 * 如果返回数组获取list则会按照顺序用分隔符拼接
	 * </p>
	 * <p>
	 * 示例: #p0: 返回第一个参数
	 * </p>
	 * <p>
	 * 示例: {#p0,#p1}: 返回数组: [参数1, 参数2]
	 * </p>
	 */
	String keyJoint() default "";

	/**
	 * 超时时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
	 */
	long ttl() default 0;

	/**
	 * 控制时长单位，默认为 SECONDS 秒
	 * @return {@link TimeUnit}
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 锁竞争失败时的重试次数
	 * @return 负数: 无限重试， 0: 不重试， 正数: 重试次数
	 */
	int retryCount() default 3;

}
