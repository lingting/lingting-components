package live.lingting.component.redis.serialize;

import live.lingting.component.redis.prefix.IRedisPrefixConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * @author Hccake
 * @version 1.0
 */
@Slf4j
public class PrefixJdkRedisSerializer extends JdkSerializationRedisSerializer {

	private final IRedisPrefixConverter redisPrefixConverter;

	public PrefixJdkRedisSerializer(IRedisPrefixConverter redisPrefixConverter) {
		this.redisPrefixConverter = redisPrefixConverter;
	}

	@Override
	public Object deserialize(byte[] bytes) {
		byte[] unwrap = redisPrefixConverter.unwrap(bytes);
		return super.deserialize(unwrap);
	}

	@Override
	public byte[] serialize(Object object) {
		byte[] originBytes = super.serialize(object);
		return redisPrefixConverter.wrap(originBytes);
	}

}
