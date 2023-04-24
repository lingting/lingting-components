package live.lingting.component.redis.operation;

import live.lingting.component.redis.operation.function.VoidMethod;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Hccake
 * @version 1.0
 */
public class CacheDelsOps extends AbstractCacheOps {

	/**
	 * 删除缓存数据
	 */
	private final VoidMethod[] cacheDels;

	public CacheDelsOps(ProceedingJoinPoint joinPoint, VoidMethod[] cacheDels) {
		super(joinPoint);
		this.cacheDels = cacheDels;
	}

	public VoidMethod[] cacheDel() {
		return cacheDels;
	}

}
