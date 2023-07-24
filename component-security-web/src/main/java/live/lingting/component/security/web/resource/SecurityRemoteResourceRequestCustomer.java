package live.lingting.component.security.web.resource;

import okhttp3.Request;

/**
 * @author lingting 2023-06-13 17:48
 */
public interface SecurityRemoteResourceRequestCustomer {

	default Request.Builder apply(Request.Builder builder) {
		return builder;
	}

	/**
	 * 解析请求构建
	 */
	default Request resolve(Request.Builder builder) {
		return builder.build();
	}

}
