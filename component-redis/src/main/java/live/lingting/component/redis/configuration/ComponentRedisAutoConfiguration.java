package live.lingting.component.redis.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.lingting.component.redis.RedisTemplateBeanPostProcessor;
import live.lingting.component.redis.core.CacheStringAspect;
import live.lingting.component.redis.prefix.IRedisPrefixConverter;
import live.lingting.component.redis.prefix.impl.DefaultRedisPrefixConverter;
import live.lingting.component.redis.properties.CacheProperties;
import live.lingting.component.redis.properties.CachePropertiesHolder;
import live.lingting.component.redis.serialize.CacheSerializer;
import live.lingting.component.redis.serialize.JacksonSerializer;
import live.lingting.component.redis.serialize.PrefixJdkRedisSerializer;
import live.lingting.component.redis.serialize.PrefixStringRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author lingting 2023-04-07 17:46
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class ComponentRedisAutoConfiguration {

	private final RedisConnectionFactory redisConnectionFactory;

	/**
	 * 初始化配置类
	 * @return GlobalCacheProperties
	 */
	@Bean
	@ConditionalOnMissingBean
	public CachePropertiesHolder cachePropertiesHolder(CacheProperties cacheProperties) {
		CachePropertiesHolder cachePropertiesHolder = new CachePropertiesHolder();
		cachePropertiesHolder.setCacheProperties(cacheProperties);
		return cachePropertiesHolder;
	}

	/**
	 * 默认使用 Jackson 序列化
	 * @param objectMapper objectMapper
	 * @return JacksonSerializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheSerializer cacheSerializer(ObjectMapper objectMapper) {
		return new JacksonSerializer(objectMapper);
	}

	/**
	 * redis key 前缀处理器
	 * @return IRedisPrefixConverter
	 */
	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = "key-prefix")
	@ConditionalOnMissingBean(IRedisPrefixConverter.class)
	public IRedisPrefixConverter redisPrefixConverter() {
		return new DefaultRedisPrefixConverter(CachePropertiesHolder.keyPrefix());
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	public StringRedisTemplate stringRedisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		return template;
	}

	@Bean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixJdkRedisSerializer(redisPrefixConverter));
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisTemplateBeanPostProcessor redisTemplateBeanPostProcessor() {
		return new RedisTemplateBeanPostProcessor();
	}

	/**
	 * 缓存注解操作切面</br>
	 * 必须在 redisHelper 初始化之后使用
	 * @param stringRedisTemplate 字符串存储的Redis操作类
	 * @param cacheSerializer 缓存序列化器
	 * @return CacheStringAspect 缓存注解操作切面
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheStringAspect cacheStringAspect(StringRedisTemplate stringRedisTemplate, CacheSerializer cacheSerializer,
			RedisTemplateBeanPostProcessor postProcessor) {
		return new CacheStringAspect(stringRedisTemplate, cacheSerializer);
	}

}
