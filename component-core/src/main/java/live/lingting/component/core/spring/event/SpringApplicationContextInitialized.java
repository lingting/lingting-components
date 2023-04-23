package live.lingting.component.core.spring.event;

import live.lingting.component.core.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 监听spring上下文初始化完成事件
 *
 * @author lingting 2022/10/15 15:27
 */
@Slf4j
public class SpringApplicationContextInitialized implements ApplicationListener<ApplicationContextInitializedEvent> {

	@Override
	public void onApplicationEvent(ApplicationContextInitializedEvent event) {
		log.debug("spring application context initialized");
		// 给 spring utils 注入 spring 上下文
		SpringUtils.setContext(event.getApplicationContext());
	}

}
