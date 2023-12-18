package live.lingting.component.security.resource;

import live.lingting.component.security.store.SecurityStore;
import live.lingting.component.security.token.SecurityToken;
import lombok.RequiredArgsConstructor;

/**
 * @author lingting 2023-12-15 15:57
 */
@RequiredArgsConstructor
public class SecurityDefaultResourceServiceImpl implements SecurityResourceService {

	private final SecurityStore store;

	@Override
	public SecurityScope resolve(SecurityToken token) {
		return store.get(token.getToken());
	}

}
