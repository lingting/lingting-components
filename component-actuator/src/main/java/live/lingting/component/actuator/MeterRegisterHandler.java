package live.lingting.component.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author lingting 2023-07-25 18:30
 */
@RequiredArgsConstructor
public class MeterRegisterHandler implements InitializingBean {

	private final MeterRegistry registry;

	private final List<MeterBinder> binders;

	public void registryAll() {
		for (MeterBinder binder : binders) {
			binder.bindTo(registry);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		registryAll();
	}

}
