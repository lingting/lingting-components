package live.lingting.component.jackson.sensitive;

import live.lingting.component.core.util.StringUtils;
import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-04-27 15:42
 */
@UtilityClass
public class SensitiveUtils {

	public static final String MIDDLE = "******";

	/**
	 * 脱敏字符串序列化
	 * @param raw 原始字符串
	 * @param prefixLength 结果前缀长度
	 * @param suffixLength 结果后缀长度
	 */
	public static String serialize(String raw, String middle, int prefixLength, int suffixLength) {
		if (!StringUtils.hasText(raw)) {
			return "";
		}

		// 如果关闭脱敏
		if (!SensitiveHolder.allowSensitive()) {
			return raw;
		}

		StringBuilder builder = new StringBuilder();

		// 开头
		builder.append(raw, 0, prefixLength);

		// 中间
		if (raw.length() > prefixLength) {
			builder.append(middle);
		}

		// 有没有结尾
		if (raw.length() > prefixLength + suffixLength) {
			builder.append(raw, raw.length() - suffixLength, raw.length());
		}

		return builder.toString();
	}

	public static String serialize(String raw, int prefixLength, int suffixLength) {
		return serialize(raw, MIDDLE, prefixLength, suffixLength);
	}

	public static String serialize(String raw, int prefixLength, int suffixLength, Sensitive sensitive) {
		if (sensitive != null) {
			if (sensitive.prefixLength() > -1) {
				prefixLength = sensitive.prefixLength();
			}
			if (sensitive.suffixLength() > -1) {
				suffixLength = sensitive.suffixLength();
			}

			if (StringUtils.hasText(sensitive.middle())) {
				return serialize(raw, sensitive.middle(), prefixLength, suffixLength);
			}
		}
		return serialize(raw, prefixLength, suffixLength);
	}

}
