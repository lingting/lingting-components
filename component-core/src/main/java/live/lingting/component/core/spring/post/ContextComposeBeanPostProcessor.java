package live.lingting.component.core.spring.post;

import live.lingting.component.core.ContextComponent;
import live.lingting.component.core.spring.ComponentBeanPostProcessor;

/**
 * @author lingting 2022/10/22 15:10
 */
public class ContextComposeBeanPostProcessor implements ComponentBeanPostProcessor {

	@Override
	public boolean isProcess(Object bean, String beanName, boolean isBefore) {
		return bean != null && ContextComponent.class.isAssignableFrom(bean.getClass());
	}

	@Override
	public Object postProcessAfter(Object bean, String beanName) {
		((ContextComponent) bean).onApplicationStart();
		return bean;
	}

}
