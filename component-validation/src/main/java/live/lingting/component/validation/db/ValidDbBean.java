package live.lingting.component.validation.db;

import java.io.Serializable;

/**
 * @author lingting 2022/11/3 10:26
 */
public interface ValidDbBean {

	/**
	 * 校验指定值所代表的数据是否存在
	 * @param validDb 注解
	 * @param val 值
	 * @param obj 值所属对象, 用于扩展
	 * @return boolean true 表示通过校验
	 */
	boolean valid(ValidDb validDb, Serializable val, Object obj);

}
