package live.lingting.component.core.function;

/**
 * @author lingting 2023/1/21 22:56
 */
@FunctionalInterface
@SuppressWarnings("java:S112")
public interface ThrowingSupplier<T> {

	T get() throws Exception;

}
