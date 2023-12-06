package live.lingting.component.spring.event;

import live.lingting.component.core.context.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author lingting 2023-12-06 17:18
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		log.debug("spring application starting");
		ContextHolder.start();
	}

}
