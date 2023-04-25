package live.lingting.component.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import live.lingting.component.core.page.PageLimitParams;
import live.lingting.component.core.page.PageLimitResult;
import live.lingting.component.mybatis.util.PageUtils;

/**
 * @author lingting 2022/9/26 17:07
 */
public interface ExtendMapper<T> extends BaseMapper<T> {

	default Page<T> toIpage(PageLimitParams params) {
		return PageUtils.toIpage(params);
	}

	default PageLimitResult<T> selectPage(PageLimitParams limit, Wrapper<T> queryWrapper) {
		Page<T> page = toIpage(limit);

		Page<T> tPage = selectPage(page, queryWrapper);
		return PageLimitResult.of(tPage.getRecords(), tPage.getTotal());
	}

}
