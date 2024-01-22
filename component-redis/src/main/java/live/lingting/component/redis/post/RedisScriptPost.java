package live.lingting.component.redis.post;

import live.lingting.component.redis.script.RepeatRedisScript;

import java.util.Collection;

/**
 * @author lingting 2024-01-22 10:38
 */
@SuppressWarnings("java:S1452")
public interface RedisScriptPost {

	Collection<RepeatRedisScript<?>> loadScripts();

}
