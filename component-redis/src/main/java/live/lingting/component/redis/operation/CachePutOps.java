package live.lingting.component.redis.operation;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.Consumer;

/**
 * @author Hccake
 * @version 1.0
 */
public class CachePutOps extends AbstractCacheOps {

	/**
	 * 向缓存写入数据
	 */
	private final Consumer<Object> cachePut;

	public CachePutOps(ProceedingJoinPoint joinPoint, Consumer<Object> cachePut) {
		super(joinPoint);
		this.cachePut = cachePut;
	}

	public Consumer<Object> cachePut() {
		return cachePut;
	}

}
