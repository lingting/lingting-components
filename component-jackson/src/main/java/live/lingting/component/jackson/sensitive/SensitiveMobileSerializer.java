package live.lingting.component.jackson.sensitive;

import live.lingting.component.core.constant.GlobalConstants;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveMobileSerializer extends AbstractSensitiveSerializer {

	@Override
	public String serialize(String raw) {
		if (raw.startsWith(GlobalConstants.PLUS)) {
			String serialize = SensitiveUtils.serialize(raw.substring(1), 2, 2, sensitive);
			return GlobalConstants.PLUS + serialize;
		}
		return SensitiveUtils.serialize(raw, 2, 2, sensitive);
	}

}
