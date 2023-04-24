package live.lingting.component.easyexcel.annotation;

import com.alibaba.excel.read.builder.AbstractExcelReaderParameterBuilder;
import live.lingting.component.easyexcel.handler.DefaultAnalysisEventListener;
import live.lingting.component.easyexcel.handler.ListAnalysisEventListener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导入excel
 *
 * @author lengleng
 * @author L.cm
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("java:S1452")
public @interface RequestExcel {

	/**
	 * 前端上传字段名称 file
	 */
	String fileName() default "file";

	/**
	 * 读取的监听器类
	 * @return readListener
	 */
	Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

	/**
	 * 是否跳过空行
	 * @return 默认跳过
	 */
	boolean ignoreEmptyRow() default false;

	/**
	 * Count the number of added heads when read sheet. 0 - This Sheet has no head ,since
	 * the first row are the data 1 - This Sheet has one row head , this is the default 2
	 * - This Sheet has two row head ,since the third row is the data
	 * @see AbstractExcelReaderParameterBuilder#headRowNumber
	 * @return headRowNumber
	 */
	int headRowNumber() default 1;

}
