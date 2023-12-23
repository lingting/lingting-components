package live.lingting.component.spring.event;

import live.lingting.component.core.context.ContextComponent;
import live.lingting.component.core.context.ContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lingting 2022/10/22 17:45
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringContextClosedListener implements ApplicationListener<ContextClosedEvent> {

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.debug("spring context closed");
		ContextHolder.stop();
		ApplicationContext applicationContext = event.getApplicationContext();
		// 上下文容器停止
		contextComponentStop(applicationContext);
	}

	void contextComponentStop(ApplicationContext applicationContext) {
		Map<String, ContextComponent> map = applicationContext.getBeansOfType(ContextComponent.class);
		// 依照spring生态的@Order排序, 优先级高的先执行停止
		List<ContextComponent> values = map.values()
			.stream()
			.sorted(AnnotationAwareOrderComparator.INSTANCE)
			.collect(Collectors.toList());

		log.debug("context component stop before");
		for (ContextComponent component : values) {
			log.trace("class [{}] stop before", component.getClass());
			component.onApplicationStopBefore();
		}

		log.debug("context component stop");
		for (ContextComponent component : values) {
			log.trace("class [{}] stop", component.getClass());
			component.onApplicationStop();
		}
	}

}
