package live.lingting.component.security.web.resource;

import live.lingting.component.core.constant.GlobalConstants;
import live.lingting.component.core.constant.HttpConstants;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.token.SecurityToken;
import live.lingting.component.security.vo.AuthorizationVO;
import live.lingting.component.security.web.constant.SecurityWebConstants;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;

/**
 * @author lingting 2023-03-31 10:53
 */
@Slf4j
public class SecurityWebDefaultRemoteResourceServiceImpl implements SecurityResourceService {

	protected final OkHttpClient client;

	protected final SecurityWebRemoteResourceRequestCustomer customer;

	protected final String urlResolve;

	public SecurityWebDefaultRemoteResourceServiceImpl(String host, OkHttpClient client,
			SecurityWebRemoteResourceRequestCustomer customer) {
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
		// 请求token
		log.trace("从远端解析token: {}", token);
		AuthorizationVO vo = customer.doRequest(client, () -> {
			Request.Builder builder = resolveBuilder(token);
			return customer.resolve(builder);
		});
		log.trace("从远端获取到授权: {}", vo);
		return customer.toScope(vo);
	}

	protected Request.Builder fillSecurity(Request.Builder builder, SecurityToken token) {
		builder.header(SecurityWebConstants.TOKEN_HEADER, token.getRaw());
		return builder;
	}

	protected String join(String host, String uri) {
		if (!StringUtils.hasText(host)) {
			throw new IllegalArgumentException("remoteHost is not Null!");
		}
		StringBuilder builder = new StringBuilder(host);
		if (!host.startsWith(HttpConstants.HTTP)) {
			builder.append(HttpConstants.HTTP).append(HttpConstants.URL_DELIMITER);
		}

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
