package live.lingting.component.redis.operation;

import live.lingting.component.core.function.ThrowingFunction;
import live.lingting.component.redis.core.KeyGenerator;
import live.lingting.component.redis.core.annotation.CachePut;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.Consumer;

/**
 * @author lingting 2023-10-12 11:13
 */
@Slf4j
public class CachePutOps extends AbstractCacheOps {

	private final ProceedingJoinPoint point;

	private final ThrowingFunction<Object, String> serializer;

	@Getter
	private final String key;

	private Consumer<Object> put;

	public CachePutOps(ProceedingJoinPoint point, CachePut annotation, KeyGenerator generator,
			ThrowingFunction<Object, String> serializer) {
		this.point = point;
		this.serializer = serializer;
		this.key = generator.getKey(annotation.key(), annotation.keyJoint());
	}

	public CachePutOps put(Consumer<Object> consumer) {
		this.put = consumer;
		return this;
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
	public Object process() throws Throwable {
		Object object = point.proceed();
		String value = dbToCache(object, serializer);
		put(value);
		return object;
	}

}
