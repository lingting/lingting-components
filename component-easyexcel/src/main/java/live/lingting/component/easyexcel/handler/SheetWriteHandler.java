package live.lingting.component.easyexcel.handler;

import live.lingting.component.easyexcel.annotation.ResponseExcel;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lengleng
 * <p>
 * sheet 写出处理器
 */
public interface SheetWriteHandler {

	/**
	 * 是否支持
	 * @param obj 返回对象
	 * @return boolean
	 */
	boolean support(Object obj);

	/**
	 * 校验
	 * @param responseExcel 注解
	 */
	void check(ResponseExcel responseExcel);

	/**
	 * 返回的对象
	 * @param o obj
	 * @param response 输出对象
	 * @param responseExcel 注解
	 */
	void export(Object o, HttpServletResponse response, ResponseExcel responseExcel);

	/**
	 * 写成对象
	 * @param o obj
	 * @param response 输出对象
	 * @param responseExcel 注解
	 */
	void write(Object o, HttpServletResponse response, ResponseExcel responseExcel);

}
