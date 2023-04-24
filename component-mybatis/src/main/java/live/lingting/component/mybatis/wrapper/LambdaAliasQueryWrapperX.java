package live.lingting.component.mybatis.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import live.lingting.component.mybatis.alias.TableAlias;
import live.lingting.component.mybatis.alias.TableAliasHelper;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成可携带表别名的查询条件 当前实体必须被配置表列名注解
 *
 * @author Hccake 2021/1/14
 * @version 1.0
 * @see TableAlias
 */
public class LambdaAliasQueryWrapperX<T> extends LambdaQueryWrapperX<T> {

	private final String tableAlias;

	/**
	 * 带别名的查询列 sql 片段，默认为null，第一次使用时加载<br/>
	 * eg. t.id,t.name
	 */
	private String allAliasSqlSelect = null;

	public LambdaAliasQueryWrapperX(T entity) {
		super(entity);
		this.tableAlias = TableAliasHelper.tableAlias(getEntityClass());
	}

	public LambdaAliasQueryWrapperX(Class<T> entityClass) {
		super(entityClass);
		this.tableAlias = TableAliasHelper.tableAlias(getEntityClass());
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
	 */
	LambdaAliasQueryWrapperX(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
			Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql,
			SharedString sqlComment, SharedString sqlFirst) {
		super(entity, entityClass, sqlSelect, paramNameSeq, paramNameValuePairs, mergeSegments, lastSql, sqlComment,
				sqlFirst);
		this.tableAlias = TableAliasHelper.tableAlias(getEntityClass());
	}

	/**
	 * 获取查询带别名的查询字段 暂时没有想到好的方法进行查询字段注入 本来的想法是 自定义注入 SqlFragment 但是目前 mybatis-plus 的
	 * TableInfo 解析在 xml 解析之后进行，导致 include 标签被提前替换， 先在 wrapper 中做简单处理吧
	 * @return String allAliasSqlSelect
	 */
	public String getAllAliasSqlSelect() {
		if (allAliasSqlSelect == null) {
			allAliasSqlSelect = TableAliasHelper.tableAliasSelectSql(getEntityClass());
		}
		return allAliasSqlSelect;
	}

	/**
	 * 用于生成嵌套 sql
	 * <p>
	 * 故 sqlSelect 不向下传递
	 * </p>
	 */
	@Override
	protected LambdaAliasQueryWrapperX<T> instance() {
		return new LambdaAliasQueryWrapperX<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
				new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(),
				SharedString.emptyString());
	}

	/**
	 * 查询条件构造时添加上表别名
	 * @param column 字段Lambda
	 * @return 表别名.字段名，如：t.id
	 */
	@Override
	protected String columnToString(SFunction<T, ?> column) {
		if (column instanceof ColumnFunction) {
			@SuppressWarnings("unchecked")
			ColumnFunction<T> columnFunction = (ColumnFunction<T>) column;
			return columnFunction.apply(null);
		}
		String columnName = super.columnToString(column, true);
		return tableAlias == null ? columnName : tableAlias + "." + columnName;
	}

}
