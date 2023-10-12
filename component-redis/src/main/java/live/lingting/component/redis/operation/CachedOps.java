package live.lingting.component.redis.operation;

import live.lingting.component.core.function.ThrowingFunction;
import live.lingting.component.redis.core.KeyGenerator;
import live.lingting.component.redis.core.annotation.Cached;
import live.lingting.component.redis.lock.DistributedLock;
import live.lingting.component.redis.properties.CachePropertiesHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author lingting 2023-10-12 11:13
 */
@Slf4j
public class CachedOps extends AbstractCacheOps {

	private final ProceedingJoinPoint point;

	private final ThrowingFunction<String, Object> deserializer;

	private final ThrowingFunction<Object, String> serializer;

	@Getter
	private final String key;

	@Getter
	private final String lockKey;

	private Supplier<String> query;

	private Consumer<Object> put;

	public CachedOps(ProceedingJoinPoint point, Cached annotation, KeyGenerator generator,
			ThrowingFunction<String, Object> deserializer, ThrowingFunction<Object, String> serializer) {
		this.point = point;
		this.deserializer = deserializer;
		this.serializer = serializer;
		this.key = generator.getKey(annotation.key(), annotation.keyJoint());
		// redis 分布式锁的 key
		this.lockKey = key + CachePropertiesHolder.lockKeySuffix();
	}

	public CachedOps query(Supplier<String> supplier) {
		this.query = supplier;
		return this;
	}

	public CachedOps put(Consumer<Object> consumer) {
		this.put = consumer;
		return this;
	}

	public String query() {
		if (query == null) {
			log.warn("cache query error, query is null; key: {}", key);
			return null;
		}
		return query.get();
	}

	public void put(Object obj) {
		if (put == null) {
			log.warn("cache put error, put is null; key: {}", key);
		}
		else {
			put.accept(obj);
		}
	}

	@Override
	public Object process() throws Exception {
		// 1.==================尝试从缓存获取数据==========================
		String cache = query();
		if (nullValue(cache)) {
			return null;
		}
		if (cache != null) {
			return deserializer.apply(cache);
		}
		// 2.==========如果缓存为空 则需查询数据库并更新===============
		cache = DistributedLock.<String>instance().action(lockKey, () -> {
			// 再次查询
			String value = query();
			if (value != null) {
				return value;
			}

			// 从数据库查询数据
			Object object = point.proceed();
			// 如果数据库中没数据，填充一个String，防止缓存击穿
			value = dbToCache(object, serializer);
			put(value);
			return value;
		}).onLockFail(this::query).lock();

		// 自旋时间内未获取到锁，或者数据库中数据为空，返回null
		if (cache == null || nullValue(cache)) {
			return null;
		}
		return deserializer.apply(cache);
	}

}
