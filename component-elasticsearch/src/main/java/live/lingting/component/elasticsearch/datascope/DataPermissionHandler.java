package live.lingting.component.elasticsearch.datascope;

import java.util.List;

/**
 * @author lingting 2023-06-27 11:04
 */
public interface DataPermissionHandler {

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	List<DataScope> dataScopes();

	/**
	 * 根据权限注解过滤后的数据范围集合
	 * @param index 索引
	 * @return 数据范围集合
	 */
	List<DataScope> filterDataScopes(String index);

	/**
	 * 是否忽略权限控制，用于及早的忽略控制，例如管理员直接放行，而不必等到DataScope中再进行过滤处理，提升效率
	 * @return boolean true: 忽略，false: 进行权限控制
	 * @param dataScopeList 当前应用的 dataScope 集合
	 * @param index 索引
	 */
	boolean ignorePermissionControl(List<DataScope> dataScopeList, String index);

}
