package live.lingting.component.redis;

import live.lingting.component.core.spring.ComponentBeanPostProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author lingting 2023-04-25 14:11
 */
@SuppressWarnings("unchecked")
public class RedisTemplateBeanPostProcessor implements ComponentBeanPostProcessor {

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return !isBefore && bean instanceof StringRedisTemplate;
	}

	@Override
	public Object postProcessAfter(Object bean, String beanName) {
		RedisHelper.setRedisTemplate((RedisTemplate<String, String>) bean);
		return bean;
	}

}
