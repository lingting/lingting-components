package live.lingting.component.redis.post;

import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.redis.RedisHelper;
import live.lingting.component.redis.script.RepeatRedisScript;
import live.lingting.component.spring.post.SpringBeanPostProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lingting 2023-04-25 14:11
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class RedisTemplateBeanPostProcessor implements SpringBeanPostProcessor {

	private final List<RedisScriptPost> scriptPosts;

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return !isBefore && bean instanceof StringRedisTemplate;
	}

	@Override
	public Object postProcessAfter(Object bean, String beanName) {
		RedisTemplate<String, String> template = (RedisTemplate<String, String>) bean;
		RedisHelper.setRedisTemplate(template);

		if (!CollectionUtils.isEmpty(scriptPosts)) {
			loadScript(template);
		}
		return bean;
	}

	protected void loadScript(RedisTemplate<?, ?> template) {
		List<RepeatRedisScript<?>> scripts = new ArrayList<>();
		for (RedisScriptPost post : scriptPosts) {
			Collection<RepeatRedisScript<?>> s = post.loadScripts();
			if (!CollectionUtils.isEmpty(s)) {
				scripts.addAll(s);
			}
		}

		template.execute((RedisCallback<Object>) connection -> {
			for (RepeatRedisScript<?> script : scripts) {
				try {
					script.load(connection);
				}
				catch (Exception e) {
					log.error("redis script load error! script: {}", script.getSource(), e);
					throw e;
				}
			}

			return null;
		});
	}

}
