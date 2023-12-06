package live.lingting.component.rocketmq.consumer;

import live.lingting.component.rocketmq.RocketMqMessage;
import live.lingting.component.rocketmq.RocketMqMessageConsumer;
import live.lingting.component.rocketmq.RocketMqTarget;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import live.lingting.component.core.util.StringUtils;
import lombok.Getter;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static live.lingting.component.rocketmq.util.RocketMqUtils.ofExt;

/**
 * @author lingting 2023-10-19 15:43
 */
@Getter
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

	protected Consumer<I> operator = null;

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
	public C operator(Consumer<I> operator) {
		this.operator = operator;
		return (C) this;
	}

	public C properties(RocketMqProperties properties) {
		return address(properties.address());
	}

	public RocketMqMessage of(MessageExt ext) {
		return ofExt(group, ext, charset);
	}

	public abstract MessageModel getModel();

	public abstract C start(RocketMqMessageConsumer messageConsumer) throws MQClientException;

	public abstract void stop();

	public String getTags() {
		return StringUtils.hasText(tags) ? tags : null;
	}

	public abstract long diff();

}
