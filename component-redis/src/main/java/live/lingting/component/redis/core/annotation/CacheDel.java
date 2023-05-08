package live.lingting.component.redis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hccake
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
@Repeatable(CacheDels.class)
public @interface CacheDel {

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
	 * <p>
	 * 是否删除缓存空间{@link #key}下的所有条目
	 * </p>
	 * <p>
	 * 默认情况下，只删除相关键下的值。
	 * </p>
	 * <p>
	 * 注意，设置该参数为{@code true}时，指定的 {@link #keyJoint} 将被忽略.
	 * </p>
	 */
	boolean allEntries() default false;

}
