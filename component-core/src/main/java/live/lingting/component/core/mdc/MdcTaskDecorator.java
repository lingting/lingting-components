package live.lingting.component.core.mdc;

import cn.hutool.core.map.MapUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author lingting 2022/11/1 16:04
 */
public class MdcTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		try {
			Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
			return () -> {
				// 现在：@Async线程上下文！ 恢复Web线程上下文的MDC数据
				if (MapUtil.isNotEmpty(copyOfContextMap)) {
					MDC.setContextMap(copyOfContextMap);
				}
				runnable.run();
			};
		}
		finally {
			MDC.clear();
		}
	}

}
