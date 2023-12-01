package live.lingting.component.redis.core;

import live.lingting.component.redis.core.annotation.CacheDel;
import live.lingting.component.redis.core.annotation.CacheDels;
import live.lingting.component.redis.core.annotation.CachePut;
import live.lingting.component.redis.core.annotation.Cached;
import live.lingting.component.redis.operation.CacheDelOps;
import live.lingting.component.redis.operation.CachePutOps;
import live.lingting.component.redis.operation.CachedOps;
import live.lingting.component.redis.properties.CachePropertiesHolder;
import live.lingting.component.redis.serialize.CacheSerializer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 为保证缓存更新无异常，该切面优先级必须高于事务切面
 *
 * @author Hccake
 * @version 1.0
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class CacheStringAspect {

	Logger log = LoggerFactory.getLogger(CacheStringAspect.class);

	protected final CacheSerializer cacheSerializer;

	protected final StringRedisTemplate redisTemplate;

	protected final ValueOperations<String, String> operations;

	public CacheStringAspect(StringRedisTemplate redisTemplate, CacheSerializer cacheSerializer) {
		this.cacheSerializer = cacheSerializer;
		this.redisTemplate = redisTemplate;
		this.operations = redisTemplate.opsForValue();
	}

	@Pointcut("execution(@(@live.lingting.component.redis.core.annotation.MetaCacheAnnotation *) * *(..))")
	public void pointCut() {
		// do nothing
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		// 获取目标方法
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		if (log.isTraceEnabled()) {
			log.trace("=======The string cache aop is executed! method : {}", method.getName());
		}

		// 根据方法的参数 以及当前类对象获得 keyGenerator
		Object target = point.getTarget();
		Object[] arguments = point.getArgs();
		KeyGenerator keyGenerator = new KeyGenerator(target, method, arguments);

		// 缓存处理
		Cached cachedAnnotation = AnnotationUtils.getAnnotation(method, Cached.class);
		if (cachedAnnotation != null) {
			Type returnType = method.getGenericReturnType();
			CachedOps ops = new CachedOps(point, cachedAnnotation, keyGenerator,
					str -> cacheSerializer.deserialize(str, returnType), cacheSerializer::serialize,
					cachedAnnotation.retryCount());

			long ttl = cachedAnnotation.ttl();
			TimeUnit unit = cachedAnnotation.timeUnit();

			return ops.query(prodCacheQueryFunction(ops.getKey(), ttl, unit))
				.put(prodCachePutFunction(ops.getKey(), ttl, unit))
				.process();
		}

		// 缓存更新处理
		CachePut cachePutAnnotation = AnnotationUtils.getAnnotation(method, CachePut.class);
		if (cachePutAnnotation != null) {
			CachePutOps ops = new CachePutOps(point, cachePutAnnotation, keyGenerator, cacheSerializer::serialize);

			return ops.put(prodCachePutFunction(ops.getKey(), cachePutAnnotation.ttl(), cachePutAnnotation.timeUnit()))
				.process();
		}

		// 缓存删除处理
		CacheDel cacheDelAnnotation = AnnotationUtils.getAnnotation(method, CacheDel.class);
		if (cacheDelAnnotation != null) {
			return new CacheDelOps(point, cacheDelAnnotation, keyGenerator, redisTemplate::delete).process();
		}

		// 多个缓存删除处理
		CacheDels cacheDelsAnnotation = AnnotationUtils.getAnnotation(method, CacheDels.class);
		if (cacheDelsAnnotation != null) {
			return new CacheDelOps(point, cacheDelsAnnotation, keyGenerator, redisTemplate::delete).process();
		}

		return point.proceed();
	}

	protected Supplier<String> prodCacheQueryFunction(String key, long ttl, TimeUnit unit) {
		Supplier<String> cacheQuery;
		if (ttl < 0) {
			cacheQuery = () -> operations.get(key);
		}
		else if (ttl == 0) {
			long expireTime = CachePropertiesHolder.expireTime();
			cacheQuery = () -> getAndExpire(key, expireTime, unit);
		}
		else {
			cacheQuery = () -> getAndExpire(key, ttl, unit);
		}
		return cacheQuery;
	}

	protected String getAndExpire(String key, long timeout, TimeUnit unit) {
		return operations.getAndExpire(key, timeout, unit);
	}

	protected Consumer<Object> prodCachePutFunction(String key, long ttl, TimeUnit unit) {
		Consumer<Object> cachePut;
		if (ttl < 0) {
			cachePut = value -> operations.set(key, (String) value);
		}
		else if (ttl == 0) {
			cachePut = value -> operations.set(key, (String) value, CachePropertiesHolder.expireTime(), unit);
		}
		else {
			cachePut = value -> operations.set(key, (String) value, ttl, unit);
		}
		return cachePut;
	}

}
