package live.lingting.component.rocketmq.consumer;

import live.lingting.component.rocketmq.RocketMqTarget;
import live.lingting.component.rocketmq.message.RocketMqMessageConvert;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-05-26 13:34
 */
@Slf4j
@RequiredArgsConstructor
public class RocketMqConsumerProvider {

	private final RocketMqProperties properties;

	private final RocketMqMessageConvert convert;

	/**
	 * 集群模式消费
	 */
	public RocketMqConsumer.ClusterPush cluster(String group, String topic) {
		return cluster(group, topic, null);
	}

	public RocketMqConsumer.ClusterPush cluster(String group, String topic, String tags) {
		return new RocketMqConsumer.ClusterPush().properties(properties)
			.convert(convert)
			.group(group)
			.topic(topic)
			.tags(tags);
	}

	public RocketMqConsumer.ClusterPush cluster(RocketMqTarget target) {
		return cluster(target.getGroup(), target.getTopic());
	}

	public RocketMqConsumer.ClusterPull clusterPull(String group, String topic) {
		return clusterPull(group, topic, null);
	}

	public RocketMqConsumer.ClusterPull clusterPull(String group, String topic, String tags) {
		return new RocketMqConsumer.ClusterPull().properties(properties)
			.convert(convert)
			.group(group)
			.topic(topic)
			.tags(tags);
	}

	public RocketMqConsumer.ClusterPull clusterPull(RocketMqTarget target) {
		return clusterPull(target.getGroup(), target.getTopic());
	}

	/**
	 * 广播模式
	 */
	public RocketMqConsumer.BoardPush board(String group, String topic) {
		return board(group, topic, null);
	}

	public RocketMqConsumer.BoardPush board(String group, String topic, String tags) {
		return new RocketMqConsumer.BoardPush().properties(properties)
			.convert(convert)
			.group(group)
			.topic(topic)
			.tags(tags);
	}

	public RocketMqConsumer.BoardPush board(RocketMqTarget target) {
		return board(target.getGroup(), target.getTopic());
	}

	public RocketMqConsumer.BoardPull boardPull(String group, String topic) {
		return boardPull(group, topic, null);
	}

	public RocketMqConsumer.BoardPull boardPull(String group, String topic, String tags) {
		return new RocketMqConsumer.BoardPull().properties(properties)
			.convert(convert)
			.group(group)
			.topic(topic)
			.tags(tags);
	}

	public RocketMqConsumer.BoardPull boardPull(RocketMqTarget target) {
		return boardPull(target.getGroup(), target.getTopic());
	}

}
