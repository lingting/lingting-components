package live.lingting.component.bcpkix.spring;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

import java.security.Security;

/**
 * 启动时加载 bc
 *
 * @author lingting
 */
@Slf4j
@Order
public class BcpkixSpringBootInitialized implements ApplicationListener<ApplicationContextInitializedEvent> {

	@Override
	public void onApplicationEvent(ApplicationContextInitializedEvent event) {
		log.debug("load bc security provider");
		Security.addProvider(new BouncyCastleProvider());
	}

}
