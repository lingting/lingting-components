package live.lingting.component.redis.operation;

import live.lingting.component.redis.operation.function.VoidMethod;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Hccake
 * @version 1.0
 */
public class CacheDelOps extends AbstractCacheOps {

	/**
	 * 删除缓存数据
	 */
	private final VoidMethod cacheDel;

	public CacheDelOps(ProceedingJoinPoint joinPoint, VoidMethod cacheDel) {
		super(joinPoint);
		this.cacheDel = cacheDel;
	}

	public VoidMethod cacheDel() {
		return cacheDel;
	}

}
