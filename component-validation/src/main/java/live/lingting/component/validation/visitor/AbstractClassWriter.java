package live.lingting.component.validation.visitor;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.core.util.SystemUtils;

/**
 * @author lingting 2022/11/2 11:24
 */
@SuppressWarnings({ "java:S112", "java:S1610" })
public abstract class AbstractClassWriter {

	boolean isJdk8() {
		String version = SystemUtils.javaVersion();
		return StringUtils.hasText(version) && version.startsWith("1.8");
	}

	/**
	 * 写入字节码
	 */
	public void write() throws Exception {
		ClassPool pool = getPool();
		CtClass ctClass;
		try {
			ctClass = pool.get(getClassName());
		}
		catch (NotFoundException e) {
			// 找不到类, 结束
			return;
		}
		write(ctClass);
		// 转class
		if (isJdk8()) {
			ctClass.toClass();
		}
		else {
			ctClass.toClass(getPackageClass());
		}
		// 释放
		ctClass.detach();
	}

	/**
	 * 获取类池
	 * @return javassist.ClassPool
	 */
	public ClassPool getPool() {
		return ClassPool.getDefault();
	}

	/**
	 * 获取指定类的指定方法
	 * @param ctClass 类
	 * @param methodName 方法名
	 * @param paramClassNames 方法参数类名
	 * @return javassist.CtMethod
	 * @throws NotFoundException 异常
	 */
	public CtMethod getMethod(CtClass ctClass, String methodName, String... paramClassNames) throws NotFoundException {
		if (paramClassNames == null || paramClassNames.length < 1) {
			return ctClass.getDeclaredMethod(methodName);
		}
		CtClass[] classes = new CtClass[paramClassNames.length];

		ClassPool pool = getPool();
		for (int i = 0; i < paramClassNames.length; i++) {
			classes[i] = pool.get(paramClassNames[i]);
		}
		return ctClass.getDeclaredMethod(methodName, classes);
	}

	/**
	 * 获取类名
	 * @return java.lang.String
	 */
	public abstract String getClassName();

	/**
	 * 获取同包名的其他类, 用于加载修改后的类
	 * @return java.lang.Class<?> class
	 */
	public abstract Class<?> getPackageClass();

	/**
	 * 写入指定类
	 * @param ctClass 类
	 * @throws Exception 异常
	 */
	public abstract void write(CtClass ctClass) throws Exception;

}
