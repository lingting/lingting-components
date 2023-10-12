package live.lingting.component.redis.operation;

import live.lingting.component.core.function.ThrowingFunction;
import live.lingting.component.redis.properties.CachePropertiesHolder;

/**
 * @author lingting 2023-10-12 11:38
 */
public abstract class AbstractCacheOps {

	/**
	 * 检查缓存数据是否是空值
	 * @param cacheData 缓存数据
	 * @return true: 是空值
	 */
	public boolean nullValue(Object cacheData) {
		return nullValue().equals(cacheData);
	}

	public String nullValue() {
		return CachePropertiesHolder.nullValue();
	}

	public String dbToCache(Object object, ThrowingFunction<Object, String> serialize) throws Exception {
		if (object == null) {
			return nullValue();
		}
		return serialize.apply(object);
	}

	@SuppressWarnings("java:S112")
	public abstract Object process() throws Throwable;

}
