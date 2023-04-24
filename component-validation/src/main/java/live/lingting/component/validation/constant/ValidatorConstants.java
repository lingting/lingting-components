package live.lingting.component.validation.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2022/11/1 20:15
 */
@UtilityClass
public class ValidatorConstants {

	public static final String VALID_DB_BEAN = "dbValidatorBean";

	public static final String VALID_DB_DEFAULT_MESSAGE = "未找到对应的数据!";

	/**
	 * 数据已存在时默认返回的消息
	 */
	public static final String VALID_DB_EXITS_DENY_MESSAGE = "已存在相同的数据!";

}
