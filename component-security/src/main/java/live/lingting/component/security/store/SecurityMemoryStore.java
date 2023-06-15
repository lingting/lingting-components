package live.lingting.component.security.store;

import live.lingting.component.security.resource.SecurityScope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingting 2023-06-15 16:07
 */
public class SecurityMemoryStore implements SecurityStore {

	private final Map<String, SecurityScope> map = new ConcurrentHashMap<>();

	@Override
	public void save(SecurityScope scope) {
		map.put(scope.getToken(), scope);
	}

	@Override
	public void update(SecurityScope scope) {
		map.put(scope.getToken(), scope);
	}

	@Override
	public void deleted(SecurityScope scope) {
		map.remove(scope.getToken());
	}

	@Override
	public SecurityScope get(String token) {
		return map.get(token);
	}

}
