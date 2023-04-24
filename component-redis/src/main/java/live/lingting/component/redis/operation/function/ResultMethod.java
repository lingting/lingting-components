package live.lingting.component.redis.operation.function;

/**
 * @author Hccake
 * @version 1.0
 */
@FunctionalInterface
public interface ResultMethod<T> {

	/**
	 * 执行并返回一个结果
	 * @return result
	 */
	T run();

}
