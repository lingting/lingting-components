package live.lingting.component.spring.event;

import live.lingting.component.spring.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 监听spring上下文初始化完成事件
 *
 * @author lingting 2022/10/15 15:27
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringApplicationContextInitializedListener
		implements ApplicationListener<ApplicationContextInitializedEvent> {

	@Override
	public void onApplicationEvent(ApplicationContextInitializedEvent event) {
		log.debug("spring application context initialized");
		// 给 spring utils 注入 spring 上下文
		SpringUtils.setContext(event.getApplicationContext());
	}

}
