package live.lingting.component.core.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * @author lingting
 */
@UtilityClass
public class ArrayUtils {

	public static final int NOT_FOUNT = -1;

	/**
	 * 数组是否为空
	 * @param obj 对象
	 * @return true表示为空, 如果对象不为数组, 返回false
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (!obj.getClass().isArray()) {
			return false;
		}

		int length = Array.getLength(obj);
		return length < 1;
	}

	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	public static <T> int indexOf(T[] array, T val) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				T t = array[i];
				if (Objects.equals(t, val)) {
					return i;
				}
			}
		}
		return NOT_FOUNT;
	}

	public static <T> boolean contains(T[] array, T val) {
		return indexOf(array, val) > NOT_FOUNT;
	}

}
