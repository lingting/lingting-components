package live.lingting.component.core.spring.event;

import live.lingting.component.core.ContextComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * @author lingting 2022/10/22 17:45
 */
@Slf4j
@Order
@RequiredArgsConstructor
public class SpringContextClosed implements ApplicationListener<ContextClosedEvent> {

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.debug("spring context closed");
		ApplicationContext applicationContext = event.getApplicationContext();

		// 上下文容器停止
		contextComponentStop(applicationContext);
	}

	private static void contextComponentStop(ApplicationContext applicationContext) {
		Map<String, ContextComponent> contextComponentMap = applicationContext.getBeansOfType(ContextComponent.class);

		for (Map.Entry<String, ContextComponent> entry : contextComponentMap.entrySet()) {
			entry.getValue().onApplicationStop();
		}
	}

}
