package live.lingting.component.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import live.lingting.component.core.page.PageLimitParams;
import live.lingting.component.core.util.CollectionUtils;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2023-04-25 13:32
 */
@UtilityClass
public class PageUtils {

	public static <T> Page<T> toIpage(PageLimitParams params) {
		Page<T> page = new Page<>();
		page.setCurrent(params.getPage());
		page.setSize(params.getSize());

		List<PageLimitParams.Sort> sorts = params.getSorts();
		if (!CollectionUtils.isEmpty(sorts)) {
			ArrayList<OrderItem> orders = new ArrayList<>();

			for (PageLimitParams.Sort sort : sorts) {
				OrderItem item = new OrderItem();
				item.setAsc(!sort.getDesc());
				item.setColumn(sort.getField());
				orders.add(item);
			}

			page.setOrders(orders);
		}

		return page;
	}

}
