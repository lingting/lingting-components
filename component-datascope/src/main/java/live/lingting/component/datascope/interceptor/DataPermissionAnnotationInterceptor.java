package live.lingting.component.datascope.interceptor;

import live.lingting.component.datascope.annotation.DataPermission;
import live.lingting.component.datascope.handler.DataPermissionRule;
import live.lingting.component.datascope.holder.DataPermissionRuleHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * DataPermission注解的拦截器，在执行方法前将当前方法的对应注解压栈，执行后弹出注解
 *
 * @author hccake
 */
public class DataPermissionAnnotationInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		// 当前方法
		Method method = methodInvocation.getMethod();
		// 获取执行类
		Object invocationThis = methodInvocation.getThis();
		Class<?> clazz = invocationThis != null ? invocationThis.getClass() : method.getDeclaringClass();
		// 寻找对应的 DataPermission 注解属性
		DataPermission dataPermission = DataPermissionFinder.findDataPermission(method, clazz);
		// 理论上这里是不会为空的
		if (dataPermission == null) {
			return methodInvocation.proceed();
		}

		DataPermissionRuleHolder.push(new DataPermissionRule(dataPermission));
		try {
			return methodInvocation.proceed();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

}
