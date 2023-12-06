package live.lingting.component.elasticsearch.datascope;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-06-27 11:06
 */
@RequiredArgsConstructor
public class DefaultDataPermissionHandler implements DataPermissionHandler {

	protected final List<DataScope> scopes;

	@Override
	public List<DataScope> dataScopes() {
		return scopes == null ? Collections.emptyList() : Collections.unmodifiableList(scopes);
	}

	@Override
	public List<DataScope> filterDataScopes(String index) {
		if (scopes == null) {
			return Collections.emptyList();
		}
		return dataScopes().stream().filter(scope -> scope.includes(index)).collect(Collectors.toList());
	}

	@Override
	public boolean ignorePermissionControl(List<DataScope> dataScopeList, String index) {
		return false;
	}

}
