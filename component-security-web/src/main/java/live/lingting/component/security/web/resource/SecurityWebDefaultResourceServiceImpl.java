package live.lingting.component.security.web.resource;

import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.store.SecurityStore;
import live.lingting.component.security.token.SecurityToken;
import lombok.RequiredArgsConstructor;

/**
 * @author lingting 2023-12-15 15:57
 */
@RequiredArgsConstructor
public class SecurityWebDefaultResourceServiceImpl implements SecurityResourceService {

	private final SecurityStore store;

	@Override
	public SecurityScope resolve(SecurityToken token) {
		return store.get(token.getToken());
	}

}
