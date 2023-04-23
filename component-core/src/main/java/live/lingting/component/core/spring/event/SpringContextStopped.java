package live.lingting.component.core.spring.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2022/10/22 17:45
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringContextStopped implements ApplicationListener<ContextStoppedEvent> {

	@Override
	public void onApplicationEvent(ContextStoppedEvent event) {
		log.debug("spring context stoped");
	}

}
