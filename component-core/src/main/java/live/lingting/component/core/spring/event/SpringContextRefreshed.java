package live.lingting.component.core.spring.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2022/10/22 17:45
 */
@Slf4j
@Component
public class SpringContextRefreshed implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.debug("spring context refresh");
	}

}
