package live.lingting.component.security.web.resource;

import live.lingting.component.core.constant.GlobalConstants;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.security.mapstruct.SecurityMapstruct;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.token.SecurityToken;
import live.lingting.component.security.vo.AuthorizationVO;
import live.lingting.component.security.web.constant.SecurityWebConstants;
import okhttp3.Request;

/**
 * @author lingting 2023-03-31 10:53
 */
public class SecurityDefaultRemoteResourceServiceImpl implements SecurityResourceService {

	protected final OkHttpClient client;

	protected final SecurityRemoteResourceRequestCustomer customer;

	protected final String urlResolve;

	public SecurityDefaultRemoteResourceServiceImpl(String host, OkHttpClient client,
			SecurityRemoteResourceRequestCustomer customer) {
		this.client = client;
		this.customer = customer;
		this.urlResolve = join(host, SecurityWebConstants.URI_RESOLVE);
	}

	public Request.Builder resolveBuilder(SecurityToken token) {
		Request.Builder builder = new Request.Builder();
		builder.get().url(urlResolve);
		builder = fillSecurity(builder, token);
		builder = customer.apply(builder);
		return builder;
	}

	@Override
	public SecurityScope resolve(SecurityToken token) {
		Request.Builder builder = resolveBuilder(token);
		Request request = customer.resolve(builder);
		AuthorizationVO vo = client.request(request, AuthorizationVO.class);
		return SecurityMapstruct.INSTANCE.ofVo(vo);
	}

	Request.Builder fillSecurity(Request.Builder builder, SecurityToken token) {
		builder.header(SecurityWebConstants.TOKEN_HEADER,
				String.format("%s %s",
						StringUtils.hasText(token.getType()) ? token.getType() : SecurityWebConstants.TOKEN_TYPE_BEARER,
						token.getToken()));
		return builder;
	}

	String join(String host, String uri) {
		if (!StringUtils.hasText(host)) {
			throw new IllegalArgumentException("remoteHost is not Null!");
		}
		StringBuilder builder = new StringBuilder(host);
		if (!host.endsWith(GlobalConstants.SLASH)) {
			builder.append(GlobalConstants.SLASH);
		}

		if (uri.startsWith(GlobalConstants.SLASH)) {
			uri = uri.substring(1);
		}
		builder.append(uri);
		return builder.toString();
	}

}