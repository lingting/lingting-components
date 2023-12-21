package live.lingting.component.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import live.lingting.component.easyexcel.kit.EasyExcelIndexObject;

/**
 * @author lingting 2023-12-21 15:39
 */
public abstract class AbstractIndexListListener<D extends EasyExcelIndexObject> extends AbstractListListener<D> {

	@Override
	protected void invoke(D data, Integer rowIndex, AnalysisContext analysisContext) {
		data.setIndex(rowIndex);
	}

	protected void invokeAfter(D data, AnalysisContext analysisContext) {
		//
	}

}
