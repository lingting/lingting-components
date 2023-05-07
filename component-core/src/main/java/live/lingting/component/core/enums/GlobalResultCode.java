package live.lingting.component.core.enums;

import live.lingting.component.core.ResultCode;
import live.lingting.component.core.constant.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2022/9/19 13:56
 */
@Getter
@AllArgsConstructor
public enum GlobalResultCode implements ResultCode {

	/**
	 * 成功
	 */
	SUCCESS(200, "Success"),
	/**
	 * 参数异常
	 */
	PARAMS_ERROR(400, "Params Error!"),
	/**
	 * 授权异常, 身份异常
	 */
	UNAUTHORIZED_ERROR(401, "Unauthorized Error!"),
	/**
	 * 权限异常
	 */
	FORBIDDEN_ERROR(403, "Forbidden Error!"),
	/**
	 * 重复请求
	 */
	REPEAT_ERROR(409, "Repeat error!"),
	/**
	 * 系统异常
	 */
	SERVER_ERROR(500, GlobalConstants.SYSTEM_ERROR),

	// region 202200
	/**
	 * 空指针异常
	 */
	SERVER_NLP_ERROR(2022000000, GlobalConstants.SYSTEM_ERROR),
	/**
	 * 参数类型转换异常
	 */
	SERVER_PARAM_CONVERT_ERROR(2022000001, GlobalConstants.SYSTEM_ERROR),
	/**
	 * 请求方式异常
	 */
	SERVER_METHOD_ERROR(2022000002, "Request Method Error!"),
	/**
	 * 请求参数非法
	 */
	SERVER_PARAM_INVALID_ERROR(2022000003, "Request Params Invalid!"),
	/**
	 * 请求参数绑定异常
	 */
	SERVER_PARAM_BIND_ERROR(2022000004, "Request Params Bind Error!"),
	/**
	 * 请求体异常
	 */
	PARAM_BODY_ERROR(2022000005, "Request Params Body Error!"),
	/**
	 * 请求参数缺失
	 */
	PARAM_MISSING_ERROR(2022000006, "Request Params Missing Error!"),
	/**
	 * 支付异常
	 */
	PAY_ERROR(2022000007, "Pay Error!"),
	/**
	 * 数据保存异常
	 */
	SAVE_ERROR(2022000008, "Save Error!"),
	/**
	 * 数据更新异常
	 */
	UPDATE_ERROR(2022000009, "Update Error!"),
	/**
	 * 数据获取异常
	 */
	GET_ERROR(2022000010, "Data Get Error!"),
	/**
	 * 正在处理中, 请稍候!
	 */
	WAIT(2022000011, "Processing!"),
	/**
	 * 阿里云异常
	 */
	ALI_ERROR(2022000012, "阿里云相关操作异常!"),

	// endregion
	;

	private final Integer code;

	private final String message;

}
