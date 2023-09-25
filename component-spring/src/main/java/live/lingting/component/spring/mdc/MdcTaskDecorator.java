package live.lingting.component.spring.mdc;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author lingting 2022/11/1 16:04
 */
public class MdcTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		final Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
		return () -> {
			try {
				// 现在：@Async线程上下文！ 恢复Web线程上下文的MDC数据
				if (copyOfContextMap != null) {
					MDC.setContextMap(copyOfContextMap);
				}
				runnable.run();
			}
			finally {
				MDC.clear();
			}
		};
	}

}
