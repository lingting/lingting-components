package live.lingting.component.rocketmq.admin;

import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.MQAdminImpl;
import org.apache.rocketmq.client.impl.MQClientAPIImpl;
import org.apache.rocketmq.client.impl.factory.MQClientInstance;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.common.protocol.body.TopicConfigSerializeWrapper;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static live.lingting.component.rocketmq.util.RocketMqUtils.toConfig;

/**
 * @author lingting 2023-12-06 18:43
 */
@Slf4j
public class RocketMqAdmin {

	protected final ClientConfig config;

	protected final MQClientInstance instance;

	protected final MQClientAPIImpl api;

	protected final MQAdminImpl impl;

	/**
	 * 创建Topic时的用于溯源使用了哪些broker的主题
	 */
	@Getter
	@Setter
	protected String defaultTopic = "SELF_TEST_TOPIC";

	public RocketMqAdmin(RocketMqProperties properties) {
		this(toConfig(properties));
	}

	protected RocketMqAdmin(ClientConfig config) {
		this.config = config;
		this.instance = new MQClientInstance(config, 0, config.buildMQClientId());
		this.api = instance.getMQClientAPIImpl();
		this.impl = new MQAdminImpl(instance);
	}

	public Set<String> listTopic() throws RemotingException, InterruptedException, MQClientException {
		return api.getTopicListFromNameServer(getTimeout()).getTopicList();
	}

	public boolean upsertTopic(String topic, Integer queueNum) {
		try {
			impl.createTopic(getDefaultTopic(), topic, queueNum);
			return true;
		}
		catch (Exception e) {
			log.error("创建topic时异常! defaultTopic: {}; topic: {}; queueNum: {};", getDefaultTopic(), topic, queueNum, e);
			return false;
		}
	}

	/**
	 * 获取指定broker的所有topic
	 * @return key: topic, value: config
	 */
	public Map<String, TopicConfig> allTopicConfig(BrokerData broker) throws RemotingConnectException,
			RemotingSendRequestException, RemotingTimeoutException, MQBrokerException, InterruptedException {
		Optional<String> optional = broker.getBrokerAddrs().values().stream().findAny();
		if (!optional.isPresent()) {
			return Collections.emptyMap();
		}
		String address = optional.get();
		TopicConfigSerializeWrapper wrapper = api.getAllTopicConfig(address, getTimeout());
		return wrapper.getTopicConfigTable();
	}

	public List<BrokerData> listBroker() throws RemotingException, InterruptedException, MQClientException {
		return api.getTopicRouteInfoFromNameServer(getDefaultTopic(), getTimeout()).getBrokerDatas();
	}

	// region 其他

	public long getTimeout() {
		return config.getMqClientApiTimeout();
	}

	public void start() throws MQClientException {
		instance.start();
	}

	public void stop() {
		instance.shutdown();
	}

	// endregion

}
