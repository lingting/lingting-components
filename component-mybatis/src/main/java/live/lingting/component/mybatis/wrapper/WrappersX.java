package live.lingting.component.mybatis.wrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

/**
 * @author Hccake 2021/1/14
 * @version 1.0
 */
public final class WrappersX {

	private WrappersX() {
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX() {
		return new LambdaQueryWrapperX<>();
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(T entity) {
		return new LambdaQueryWrapperX<>(entity);
	}

	/**
	 * 获取 LambdaQueryWrapperX&lt;T&gt;
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaQueryWrapperX&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaQueryWrapperX<T> lambdaQueryX(Class<T> entityClass) {
		return new LambdaQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取 LambdaAliasQueryWrapper&lt;T&gt;
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaAliasQueryWrapper&lt;T&gt;
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(T entity) {
		return new LambdaAliasQueryWrapperX<>(entity);
	}

	/**
	 * 获取 LambdaAliasQueryWrapper&lt;T&gt;
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaAliasQueryWrapper&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaAliasQueryWrapperX<T> lambdaAliasQueryX(Class<T> entityClass) {
		return new LambdaAliasQueryWrapperX<>(entityClass);
	}

	/**
	 * 获取 LambdaUpdateWrapper&lt;T&gt; 复制 com.baomidou.mybatisplus.core.toolkit.Wrappers
	 * @param <T> 实体类泛型
	 * @return LambdaUpdateWrapper&lt;T&gt;
	 */
	public static <T> LambdaUpdateWrapper<T> lambdaUpdate() {
		return new LambdaUpdateWrapper<>();
	}

	/**
	 * 获取 LambdaUpdateWrapper&lt;T&gt; 复制 com.baomidou.mybatisplus.core.toolkit.Wrappers
	 * @param entity 实体类
	 * @param <T> 实体类泛型
	 * @return LambdaUpdateWrapper&lt;T&gt;
	 */
	public static <T> LambdaUpdateWrapper<T> lambdaUpdate(T entity) {
		return new LambdaUpdateWrapper<>(entity);
	}

	/**
	 * 获取 LambdaUpdateWrapper&lt;T&gt; 复制 com.baomidou.mybatisplus.core.toolkit.Wrappers
	 * @param entityClass 实体类class
	 * @param <T> 实体类泛型
	 * @return LambdaUpdateWrapper&lt;T&gt;
	 * @since 3.3.1
	 */
	public static <T> LambdaUpdateWrapper<T> lambdaUpdate(Class<T> entityClass) {
		return new LambdaUpdateWrapper<>(entityClass);
	}

}
