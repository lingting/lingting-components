package live.lingting.component.rocketmq.consumer;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.rocketmq.RocketMqTarget;
import live.lingting.component.rocketmq.message.RocketMqMessage;
import live.lingting.component.rocketmq.message.RocketMqMessageConsumer;
import live.lingting.component.rocketmq.message.RocketMqMessageConvert;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.Getter;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author lingting 2023-10-19 15:43
 */
@Getter
@SuppressWarnings("unchecked")
public abstract class AbstractConsumer<C extends AbstractConsumer<C, I>, I extends ClientConfig> {

	private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected String address;

	protected Charset charset = StandardCharsets.UTF_8;

	protected String group;

	protected String topic;

	protected String tags;

	/**
	 * 单次批量可获取的最大数量
	 * <p>
	 * push 模式下, (1, 1024)
	 * </p>
	 */
	protected int batchSize = 32;

	protected RocketMqCustomize customize = null;

	protected RocketMqMessageConvert convert;

	public C address(String address) {
		this.address = address;
		return (C) this;
	}

	public C charset(Charset charset) {
		this.charset = charset;
		return (C) this;
	}

	public C group(String group) {
		this.group = group;
		return (C) this;
	}

	public C topic(String topic) {
		this.topic = topic;
		return (C) this;
	}

	public C target(RocketMqTarget target) {
		return group(target.getGroup()).topic(target.getTopic());
	}

	public C tags(String tags) {
		this.tags = tags;
		return (C) this;
	}

	public C batchSize(int value) {
		batchSize = value;
		return (C) this;
	}

	/**
	 * 启动前触发, 允许自定义配置
	 */
	public C customize(RocketMqCustomize customize) {
		this.customize = customize;
		return (C) this;
	}

	public C convert(RocketMqMessageConvert convert) {
		this.convert = convert;
		return (C) this;
	}

	public C properties(RocketMqProperties properties) {
		return address(properties.address());
	}

	public abstract MessageModel getModel();

	public abstract C start(RocketMqMessageConsumer messageConsumer) throws MQClientException;

	public abstract void stop();

	public String getTags() {
		return StringUtils.hasText(tags) ? tags : null;
	}

	public abstract long diff();

	public RocketMqMessage ofExt(MessageExt ext) {
		String currentGroup = getGroup();
		RocketMqMessageConvert currentConvert = getConvert();
		return currentConvert.of(currentGroup, ext);
	}

	public abstract boolean isStart();

}
