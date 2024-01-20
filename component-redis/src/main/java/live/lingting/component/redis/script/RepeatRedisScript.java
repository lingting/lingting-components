package live.lingting.component.redis.script;

import live.lingting.component.redis.RedisHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lingting 2023-11-22 16:11
 */
public class RepeatRedisScript<T> implements RedisScript<T>, InitializingBean {

	private final DefaultRedisScript<T> script;

	@Getter
	protected final String sha1;

	@Getter
	protected final byte[] sha1Bytes;

	@Getter
	protected final Class<T> resultType;

	@Getter
	protected final ReturnType returnType;

	@Getter
	protected final String source;

	protected byte[] bytes;

	/**
	 * 脚本sha1是否存在于redis中
	 */
	@Setter
	protected boolean existsSha1 = false;

	/**
	 * 是否允许在 管道, 队列中使用 sha1执行
	 */
	@Setter
	protected boolean useSha1ByPipelined = false;

	protected RepeatRedisScript(DefaultRedisScript<T> script) {
		this.script = script;
		this.sha1 = script.getSha1();
		this.sha1Bytes = sha1.getBytes(StandardCharsets.UTF_8);
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
	public void afterPropertiesSet() {
		script.afterPropertiesSet();
	}

	@Override
	public String getScriptAsString() {
		return getSource();
	}

	boolean isEmptyBytes() {
		return bytes == null || bytes.length < 1;
	}

	public byte[] bytes(RedisSerializer<String> serializer) {
		byte[] serialize = serializer.serialize(getScriptAsString());
		if (isEmptyBytes()) {
			bytes = serialize;
		}
		return serialize;
	}

	public byte[] bytes() {
		if (isEmptyBytes()) {
			RedisSerializer<String> serializer = RedisHelper.getStringSerializer();
			return bytes(serializer);
		}
		return bytes;
	}

	/**
	 * 加载脚本到redis
	 */
	public void load() {
		RedisHelper.execute((RedisCallback<Object>) connection -> {
			load(connection);
			return null;
		});
	}

	public void load(RedisConnection connection) {
		connection.scriptLoad(bytes());
		existsSha1 = true;
		useSha1ByPipelined = true;
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

		// 依据sha1执行
		ScriptExecuteResult result = evalBySha1(connection, keySize, keysAndArgs);

		// 执行失败, 依据字节执行
		if (!result.isSuccess()) {
			result = evalByBytes(connection, keySize, keysAndArgs);
		}

		// 管道或者队列不需要处理返回值. 如果结果类型为null也不处理返回值
		if (connection.isPipelined() || connection.isQueueing() || getResultType() == null) {
			return null;
		}

		return ScriptUtils.deserializeResult(valueSerializer, result.getResult());
	}

	protected ScriptExecuteResult evalBySha1(RedisConnection connection, int keySize, byte[][] keysAndArgs) {
		// 是否不在 管道或者队列中使用sha1
		if (!useSha1ByPipelined && (connection.isPipelined() || connection.isQueueing())) {
			return ScriptExecuteResult.FAILED;
		}
		if (!existsSha1) {
			return ScriptExecuteResult.FAILED;
		}
		try {
			Object o = connection.evalSha(getSha1Bytes(), returnType, keySize, keysAndArgs);
			return ScriptExecuteResult.success(o);
		}
		catch (Exception e) {
			if (!ScriptUtils.exceptionContainsNoScriptError(e)) {
				throw e instanceof RuntimeException ? (RuntimeException) e
						: new RedisSystemException(e.getMessage(), e);
			}
			// 脚本不存在异常! 重置标识
			existsSha1 = false;
			return ScriptExecuteResult.FAILED;
		}
	}

	protected ScriptExecuteResult evalByBytes(RedisConnection connection, int keySize, byte[][] keysAndArgs) {
		byte[] scriptBytes = bytes();
		Object o = connection.eval(scriptBytes, returnType, keySize, keysAndArgs);
		existsSha1 = true;
		return ScriptExecuteResult.success(o);
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "java:S2259" })
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

	@Getter
	@RequiredArgsConstructor
	protected static class ScriptExecuteResult {

		public static final ScriptExecuteResult FAILED = new ScriptExecuteResult(false, null);

		private final boolean success;

		private final Object result;

		public static ScriptExecuteResult success(Object o) {
			return new ScriptExecuteResult(true, o);
		}

	}

}
