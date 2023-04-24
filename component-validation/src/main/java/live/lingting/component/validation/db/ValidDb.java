package live.lingting.component.validation.db;


import live.lingting.component.validation.constant.ValidatorConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2022/11/3 9:26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Constraint(validatedBy = ValidDbValidator.class)
public @interface ValidDb {

	String message() default ValidatorConstants.VALID_DB_DEFAULT_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 使用bean名称进行获取校验
	 */
	String beanName() default "";

	/**
	 * 使用class校验
	 */
	Class<? extends ValidDbBean> beanClas() default ValidDbBean.class;

	/**
	 * 是否校验数据已存在, 如果为false, 则会在查询到数据时校验异常
	 */
	boolean exits() default true;

}
