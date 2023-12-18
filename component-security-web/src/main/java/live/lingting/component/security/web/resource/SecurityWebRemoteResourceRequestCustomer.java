package live.lingting.component.security.web.resource;

import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.security.mapstruct.SecurityMapstruct;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.vo.AuthorizationVO;
import okhttp3.Request;

import java.util.function.Supplier;

/**
 * @author lingting 2023-06-13 17:48
 */
public interface SecurityWebRemoteResourceRequestCustomer {

	default Request.Builder apply(Request.Builder builder) {
		return builder;
	}

	/**
	 * 解析请求构建
	 */
	default Request resolve(Request.Builder builder) {
		return builder.build();
	}

	default AuthorizationVO doRequest(OkHttpClient client, Supplier<Request> supplier) {
		Request request = supplier.get();
		return client.request(request, AuthorizationVO.class);
	}

	default SecurityScope toScope(AuthorizationVO vo) {
		return SecurityMapstruct.INSTANCE.ofVo(vo);
	}

}
