package live.lingting.component.redis.serialize;

import live.lingting.component.redis.prefix.IRedisPrefixConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

/**
 * @author Hccake
 * @version 1.0
 */
@Slf4j
public class PrefixStringRedisSerializer extends StringRedisSerializer {

	private final IRedisPrefixConverter iRedisPrefixConverter;

	public PrefixStringRedisSerializer(IRedisPrefixConverter iRedisPrefixConverter) {
		super(StandardCharsets.UTF_8);
		this.iRedisPrefixConverter = iRedisPrefixConverter;
	}

	@Override
	public String deserialize(byte[] bytes) {
		byte[] unwrap = iRedisPrefixConverter.unwrap(bytes);
		return super.deserialize(unwrap);
	}

	@Override
	public byte[] serialize(String key) {
		byte[] originBytes = super.serialize(key);
		return iRedisPrefixConverter.wrap(originBytes);
	}

}
