package live.lingting.component.easyexcel.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import live.lingting.component.easyexcel.annotation.ResponseExcel;
import live.lingting.component.easyexcel.annotation.Sheet;
import live.lingting.component.easyexcel.domain.SheetBuildProperties;
import live.lingting.component.easyexcel.enhance.WriterBuilderEnhancer;
import live.lingting.component.easyexcel.kit.EasyExcelProvider;
import live.lingting.component.easyexcel.kit.ExcelException;
import live.lingting.component.easyexcel.properties.ExcelConfigProperties;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lengleng
 * <p>
 * 处理单sheet 页面
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	public SingleSheetWriteHandler(ExcelConfigProperties configProperties, EasyExcelProvider provider,
			WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, provider, excelWriterBuilderEnhance);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return boolean
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> eleList = (List<?>) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		// 获取 Sheet 配置
		SheetBuildProperties sheetBuildProperties;
		Sheet[] sheets = responseExcel.sheets();
		if (sheets != null && sheets.length > 0) {
			sheetBuildProperties = new SheetBuildProperties(sheets[0]);
		}
		else {
			sheetBuildProperties = new SheetBuildProperties(0);
		}

		// 模板信息
		String template = responseExcel.template();

		// 创建sheet
		WriteSheet sheet;
		if (eleList.isEmpty()) {
			sheet = this.emptySheet(sheetBuildProperties, template);
		}
		else {
			Class<?> dataClass = eleList.get(0).getClass();
			sheet = this.emptySheet(sheetBuildProperties, dataClass, template, responseExcel.headGenerator());
		}

		if (responseExcel.fill()) {
			// 填充 sheet
			excelWriter.fill(eleList, sheet);
		}
		else {
			// 写入 sheet
			excelWriter.write(eleList, sheet);
		}

		excelWriter.finish();
	}

}
