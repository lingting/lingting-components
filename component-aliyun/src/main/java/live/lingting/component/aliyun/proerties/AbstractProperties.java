package live.lingting.component.aliyun.proerties;

import live.lingting.component.aliyun.constant.AliConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2023-11-13 16:44
 */
@Getter
@Setter
class AbstractProperties {

	protected String protocol = AliConstants.DEFAULT_PROTOCOL;

	protected String region;

	protected String accessKey;

	protected String accessSecret;

}
