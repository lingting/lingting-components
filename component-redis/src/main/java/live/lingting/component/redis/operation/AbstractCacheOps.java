package live.lingting.component.redis.operation;

import live.lingting.component.redis.properties.CachePropertiesHolder;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Hccake
 * @version 1.0
 */
public abstract class AbstractCacheOps {

	protected AbstractCacheOps(ProceedingJoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	private final ProceedingJoinPoint joinPoint;

	/**
	 * 织入方法
	 * @return ProceedingJoinPoint
	 */
	public ProceedingJoinPoint joinPoint() {
		return joinPoint;
	}

	/**
	 * 检查缓存数据是否是空值
	 * @param cacheData 缓存数据
	 * @return true: 是空值
	 */
	public boolean nullValue(Object cacheData) {
		return CachePropertiesHolder.nullValue().equals(cacheData);
	}

}
