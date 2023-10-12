package live.lingting.component.redis.operation;

import live.lingting.component.redis.RedisHelper;
import live.lingting.component.redis.core.KeyGenerator;
import live.lingting.component.redis.core.annotation.CacheDel;
import live.lingting.component.redis.core.annotation.CacheDels;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.redis.core.Cursor;

import java.util.function.Function;

import static live.lingting.component.core.constant.GlobalConstants.ASTERISK;

/**
 * @author lingting 2023-10-12 11:47
 */
@Slf4j
public class CacheDelOps extends AbstractCacheOps {

	private final ProceedingJoinPoint point;

	private final KeyGenerator generator;

	private final Function<String, Boolean> delete;

	private final CacheDel[] annotations;

	public CacheDelOps(ProceedingJoinPoint point, CacheDel annotation, KeyGenerator generator,
			Function<String, Boolean> delete) {
		this.point = point;
		this.generator = generator;
		this.delete = delete;
		this.annotations = new CacheDel[] { annotation };
	}

	public CacheDelOps(ProceedingJoinPoint point, CacheDels annotation, KeyGenerator generator,
			Function<String, Boolean> delete) {
		this.point = point;
		this.generator = generator;
		this.delete = delete;
		this.annotations = annotation.value();
	}

	protected void delete(String key) {
		Boolean flag = delete.apply(key);
		if (!Boolean.TRUE.equals(flag)) {
			log.trace("cache delete failed! key: {}", key);
		}
	}

	protected void delete(CacheDel annotation) {
		if (delete == null) {
			log.warn("cache delete error, delete is null; key: {}", annotation.key());
			return;
		}

		// 删除单个key
		if (!annotation.allEntries()) {
			String key = generator.getKey(annotation.key(), annotation.keyJoint());
			delete(key);
			return;
		}
		// 删除名称空间
		String patten = annotation.key().concat(ASTERISK);
		try (Cursor<String> cursor = RedisHelper.scan(patten)) {
			while (cursor.hasNext()) {
				delete(cursor.next());
			}
		}
	}

	@Override
	public Object process() throws Throwable {
		// 执行目标方法
		Object object = point.proceed();

		// 开始删除
		for (CacheDel annotation : annotations) {
			delete(annotation);
		}

		return object;
	}

}
