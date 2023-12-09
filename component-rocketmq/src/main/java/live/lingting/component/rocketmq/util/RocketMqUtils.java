package live.lingting.component.rocketmq.util;

import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.experimental.UtilityClass;
import org.apache.rocketmq.client.ClientConfig;

/**
 * @author lingting 2023-12-06 18:59
 */
@UtilityClass
public class RocketMqUtils {

	public static ClientConfig toConfig(RocketMqProperties properties) {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setNamesrvAddr(properties.address());
		clientConfig.setMqClientApiTimeout((int) properties.getApiTimeout());
		return clientConfig;
	}

}
