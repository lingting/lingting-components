package live.lingting.component.dingtalk.message;

import live.lingting.component.dingtalk.DingTalkParams;
import live.lingting.component.dingtalk.enums.MessageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author lingting 2020/6/10 22:13
 */
@Getter
@Setter
@Accessors(chain = true)
public class DingTalkTextMessage extends AbstractDingTalkMessage {

	/**
	 * 消息内容
	 */
	private String content;

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.TEXT;
	}

	@Override
	public DingTalkParams put(DingTalkParams params) {
		return params.setText(new DingTalkParams.Text().setContent(content));
	}

}
