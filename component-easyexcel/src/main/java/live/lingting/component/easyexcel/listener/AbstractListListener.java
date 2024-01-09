package live.lingting.component.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import live.lingting.component.easyexcel.kit.EasyExcelIndexObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2023-04-12 20:11
 */
@Getter
public abstract class AbstractListListener<D> extends AnalysisEventListener<D> {

	private final List<D> values = new ArrayList<>();

	@Override
	public void invoke(D data, AnalysisContext analysisContext) {
		Integer rowIndex = getRowIndex(analysisContext);
		if (data instanceof EasyExcelIndexObject) {
			((EasyExcelIndexObject) data).setIndex(rowIndex);
		}
		invoke(data, rowIndex, analysisContext);
		addData(data);
	}

	protected Integer getRowIndex(AnalysisContext analysisContext) {
		return analysisContext.readRowHolder().getRowIndex();
	}

	protected void addData(D data) {
		getValues().add(data);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		//
	}

	protected void invoke(D data, Integer rowIndex, AnalysisContext analysisContext) {
		//
	}

}
