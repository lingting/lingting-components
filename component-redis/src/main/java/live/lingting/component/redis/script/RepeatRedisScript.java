package live.lingting.component.redis.script;

import live.lingting.component.core.util.ArrayUtils;
import live.lingting.component.redis.RedisHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * @author lingting 2023-11-22 16:11
 */
public class RepeatRedisScript<T> implements RedisScript<T>, InitializingBean {

	private final DefaultRedisScript<T> script;

	private final String sha1;

	private final Class<T> resultType;

	private final ReturnType returnType;

	private final String source;

	private byte[] bytes;

	public RepeatRedisScript(DefaultRedisScript<T> script) {
		this.script = script;
		this.sha1 = script.getSha1();
		this.resultType = script.getResultType();
		this.returnType = ReturnType.fromJavaType(this.resultType);
		this.source = script.getScriptAsString();
	}

	public RepeatRedisScript(String source) {
		this(source, null);
	}

	public RepeatRedisScript(String source, Class<T> cls) {
		this(new DefaultRedisScript<>(source, cls));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		script.afterPropertiesSet();
	}

	@Override
	public String getSha1() {
		return sha1;
	}

	@Override
	public Class<T> getResultType() {
		return resultType;
	}

	@Override
	public String getScriptAsString() {
		return source;
	}

	public byte[] bytes(RedisSerializer<String> serializer) {
		byte[] serialize = serializer.serialize(getScriptAsString());
		if (ArrayUtils.isEmpty(bytes)) {
			bytes = serialize;
		}
		return serialize;
	}

	public byte[] bytes() {
		if (ArrayUtils.isEmpty(bytes)) {
			RedisSerializer<String> serializer = RedisHelper.getStringSerializer();
			return bytes(serializer);
		}
		return bytes;
	}

	/**
	 * 执行脚本
	 * @see org.springframework.data.redis.core.script.DefaultScriptExecutor#execute(RedisScript,
	 * List, Object...)
	 */
	public <K> T execute(final List<K> keys, final Object... args) {
		return RedisHelper.execute((RedisCallback<T>) connection -> execute(connection, keys, args));
	}

	/**
	 * 执行脚本
	 * @see org.springframework.data.redis.core.script.DefaultScriptExecutor#execute(RedisScript,
	 * RedisSerializer, RedisSerializer, List, Object...)
	 */
	public <K> T execute(final RedisSerializer<K> keySerializer, final RedisSerializer<?> argsSerializer,
			final RedisSerializer<T> resultSerializer, final List<K> keys, final Object... args) {
		return RedisHelper.execute((RedisCallback<T>) connection -> execute(connection, keySerializer, argsSerializer,
				resultSerializer, keys, args));
	}

	/**
	 * 执行脚本
	 * @see org.springframework.data.redis.core.script.DefaultScriptExecutor#execute(RedisScript,
	 * List, Object...)
	 */
	@SuppressWarnings("unchecked")
	public <K> T execute(RedisConnection connection, final List<K> keys, final Object... args) {
		RedisTemplate<String, String> template = RedisHelper.getRedisTemplate();
		RedisSerializer<K> keySerializer = (RedisSerializer<K>) template.getKeySerializer();
		RedisSerializer<?> argsSerializer = template.getValueSerializer();
		RedisSerializer<T> valueSerializer = (RedisSerializer<T>) template.getValueSerializer();
		return execute(connection, keySerializer, argsSerializer, valueSerializer, keys, args);
	}

	/**
	 * 执行脚本
	 * @see org.springframework.data.redis.core.script.DefaultScriptExecutor#execute(RedisScript,
	 * RedisSerializer, RedisSerializer, List, Object...)
	 */
	public <K> T execute(RedisConnection connection, final RedisSerializer<K> keySerializer,
			final RedisSerializer<?> argsSerializer, final RedisSerializer<T> valueSerializer, final List<K> keys,
			final Object... args) {
		final byte[][] keysAndArgs = keysAndArgs(keySerializer, argsSerializer, keys, args);
		final int keySize = keys != null ? keys.size() : 0;
		if (connection.isPipelined() || connection.isQueueing()) {
			// We could script load first and then do evalsha to ensure sha is present,
			// but this adds a sha1 to exec/closePipeline results. Instead, just eval
			connection.eval(bytes(), returnType, keySize, keysAndArgs);
			return null;
		}
		return eval(connection, returnType, keySize, keysAndArgs, valueSerializer);

	}

	protected T eval(RedisConnection connection, ReturnType returnType, int numKeys, byte[][] keysAndArgs,
			RedisSerializer<T> valueSerializer) {

		Object result;
		try {
			result = connection.evalSha(script.getSha1(), returnType, numKeys, keysAndArgs);
		}
		catch (Exception e) {
			if (!ScriptUtils.exceptionContainsNoScriptError(e)) {
				throw e instanceof RuntimeException ? (RuntimeException) e
						: new RedisSystemException(e.getMessage(), e);
			}

			result = connection.eval(bytes(), returnType, numKeys, keysAndArgs);
		}

		if (script.getResultType() == null) {
			return null;
		}

		return ScriptUtils.deserializeResult(valueSerializer, result);
	}

	@SuppressWarnings({ "unchecked", "rawtypes","java:S2259" })
	protected <K> byte[][] keysAndArgs(RedisSerializer<K> keySerializer, RedisSerializer argsSerializer, List<K> keys,
			Object[] args) {
		final int keySize = keys != null ? keys.size() : 0;
		byte[][] keysAndArgs = new byte[args.length + keySize][];
		int i = 0;
		if (keys != null) {
			for (K key : keys) {
				if (keySerializer == null && key instanceof byte[]) {
					keysAndArgs[i++] = (byte[]) key;
				}
				else {
					keysAndArgs[i++] = keySerializer.serialize(key);
				}
			}
		}
		for (Object arg : args) {
			if (argsSerializer == null && arg instanceof byte[]) {
				keysAndArgs[i++] = (byte[]) arg;
			}
			else {
				keysAndArgs[i++] = argsSerializer.serialize(arg);
			}
		}
		return keysAndArgs;
	}

}
