package live.lingting.component.web.constant;

import lombok.experimental.UtilityClass;
import org.springframework.core.Ordered;

/**
 * @author lingting 2023-04-26 14:19
 */
@UtilityClass
public class WebConstants {

	public static final int ORDER_EXCEPTION_DEFAULT = Ordered.LOWEST_PRECEDENCE;
	public static final int ORDER_EXCEPTION_GLOBAL = 100;
}
