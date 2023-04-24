package live.lingting.component.validation;

import javassist.ClassPool;
import javassist.LoaderClassPath;
import live.lingting.component.validation.visitor.HibernateValidatorWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author lingting 2023-04-24 21:23
 */
@Slf4j
public class ValidationContextInitializedListener implements ApplicationListener<ApplicationContextInitializedEvent> {

	@SneakyThrows
	@Override
	public void onApplicationEvent(ApplicationContextInitializedEvent event) {
		ClassLoader classLoader = SpringApplication.class.getClassLoader();
		log.debug("validation注入字节码开始");
		ClassPool pool = ClassPool.getDefault();
		pool.appendClassPath(new LoaderClassPath(classLoader));

		new HibernateValidatorWriter().write();
		log.debug("validation注入字节码完成");
	}

}
