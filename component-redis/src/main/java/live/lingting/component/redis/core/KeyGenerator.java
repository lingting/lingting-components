package live.lingting.component.redis.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.redis.properties.CachePropertiesHolder;
import live.lingting.component.spring.util.SpelUtils;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 缓存key的生成工具类，主要用于解析spel, 进行拼接key的生成
 *
 * @author Hccake
 * @version 1.0
 */
public class KeyGenerator {

	/**
	 * SpEL 上下文
	 */
	protected final StandardEvaluationContext spelContext;

	public KeyGenerator(Object target, Method method, Object[] arguments) {
		this.spelContext = SpelUtils.getSpelContext(target, method, arguments);
	}

	/**
	 * 根据 keyPrefix 和 keyJoint 获取完整的 key 信息
	 * @param keyPrefix key 前缀
	 * @param keyJoint key 拼接元素，值为 spel 表达式，可为空
	 * @return 拼接完成的 key
	 */
	public String getKey(String keyPrefix, String keyJoint) {
		// 根据 keyJoint 判断是否需要拼接
		if (!StringUtils.hasText(keyJoint)) {
			return keyPrefix;
		}
		// 获取所有需要拼接的元素, 组装进集合中
		Object joint = SpelUtils.parseValue(spelContext, keyJoint);
		Assert.notNull(joint, "Key joint cannot be null!");

		String suffix;
		if (CollectionUtils.isMulti(joint)) {
			String delimiter = CachePropertiesHolder.delimiter();
			StringBuilder builder = new StringBuilder();

			Collection<Object> objects = CollectionUtils.multiToList(joint);
			for (Object obj : objects) {
				builder.append(Convert.toStr(obj)).append(delimiter);
			}
			if (builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}
			suffix = builder.toString();
		}
		else {
			suffix = Convert.toStr(joint);
		}

		if (!StringUtils.hasText(keyPrefix)) {
			return suffix;
		}
		// 拼接后返回
		return jointKey(keyPrefix, suffix);
	}

	/**
	 * 拼接key, 默认使用 ：作为分隔符
	 * @param keyItems 用于拼接 key 的元素列表
	 * @return 拼接完成的 key
	 */
	public String jointKey(List<String> keyItems) {
		return String.join(CachePropertiesHolder.delimiter(), keyItems);
	}

	/**
	 * 拼接key, 默认使用 ：作为分隔符
	 * @param keyItems 用于拼接 key 的元素列表
	 * @return 拼接完成的 key
	 */
	public String jointKey(String... keyItems) {
		return jointKey(Arrays.asList(keyItems));
	}

}
