package live.lingting.component.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import live.lingting.component.core.page.PageLimitParams;
import live.lingting.component.core.page.PageLimitResult;

import java.util.stream.Collectors;

/**
 * @author lingting 2022/9/26 17:07
 */
public interface ExtendMapper<T> extends BaseMapper<T> {

	default PageLimitResult<T> selectPage(PageLimitParams limit, Wrapper<T> queryWrapper) {
		Page<T> page = new Page<>();
		page.setCurrent(limit.getPage());
		page.setSize(limit.getSize());
		page.setOrders(limit.getSorts()
			.stream()
			.map(sort -> new OrderItem(sort.getField(), !sort.getDesc()))
			.collect(Collectors.toList()));

		Page<T> tPage = selectPage(page, queryWrapper);
		return PageLimitResult.of(tPage.getRecords(), tPage.getTotal());
	}

}
