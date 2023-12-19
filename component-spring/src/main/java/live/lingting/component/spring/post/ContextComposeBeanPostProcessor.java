package live.lingting.component.spring.post;

import live.lingting.component.core.context.ContextComponent;

/**
 * @author lingting 2022/10/22 15:10
 */
public class ContextComposeBeanPostProcessor implements SpringBeanPostProcessor {

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
