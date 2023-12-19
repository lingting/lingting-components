package live.lingting.component.convert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.protobuf.ByteString;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.jackson.JacksonUtils;
import live.lingting.component.security.mapstruct.SecurityMapstruct;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.vo.AuthorizationVO;
import live.lingting.protobuf.SecurityGrpcAuthorization;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author lingting 2023-12-18 16:04
 */
public interface SecurityGrpcConvert {

	default SecurityGrpcAuthorization.AuthorizationVO toProtobuf(AuthorizationVO vo) {
		SecurityGrpcAuthorization.AuthorizationVO.Builder builder = SecurityGrpcAuthorization.AuthorizationVO
			.newBuilder()
			.setToken(vo.getToken())
			.setTenantId(vo.getTenantId())
			.setUserId(vo.getUserId())
			.setUsername(vo.getUsername())
			.setAvatar(vo.getAvatar())
			.setNickname(vo.getNickname())
			.setIsSystem(Boolean.TRUE.equals(vo.getIsSystem()))
			.setIsEnabled(Boolean.TRUE.equals(vo.getIsEnabled()))

		;

		if (!CollectionUtils.isEmpty(vo.getRoles())) {
			builder.addAllRoles(vo.getRoles());
		}
		if (!CollectionUtils.isEmpty(vo.getPermissions())) {
			builder.addAllPermissions(vo.getPermissions());
		}

		builder.setAttributes(toBytes(vo.getAttributes()));
		return builder.buildPartial();
	}

	default AuthorizationVO toJava(SecurityGrpcAuthorization.AuthorizationVO authorizationVO) {
		AuthorizationVO vo = new AuthorizationVO();
		vo.setToken(authorizationVO.getToken());
		vo.setTenantId(authorizationVO.getTenantId());
		vo.setUserId(authorizationVO.getUserId());
		vo.setUsername(authorizationVO.getUsername());
		vo.setAvatar(authorizationVO.getAvatar());
		vo.setNickname(authorizationVO.getNickname());
		vo.setIsSystem(authorizationVO.getIsSystem());
		vo.setIsEnabled(authorizationVO.getIsEnabled());
		vo.setRoles(new HashSet<>(authorizationVO.getRolesList()));
		vo.setPermissions(new HashSet<>(authorizationVO.getPermissionsList()));
		vo.setAttributes(ofBytes(authorizationVO.getAttributes()));
		return vo;
	}

	default Charset charset() {
		return StandardCharsets.UTF_8;
	}

	default ByteString toBytes(Map<String, Object> attributes) {
		if (CollectionUtils.isEmpty(attributes)) {
			return ByteString.EMPTY;
		}
		String json = JacksonUtils.toJson(attributes);
		Charset charset = charset();
		return ByteString.copyFrom(json, charset);
	}

	default Map<String, Object> ofBytes(ByteString bytes) {
		if (bytes.isEmpty()) {
			return new HashMap<>();
		}
		Charset charset = charset();
		String json = bytes.toString(charset);

		if (!StringUtils.hasText(json) || !StringUtils.isJson(json)) {
			return new HashMap<>();
		}

		return JacksonUtils.toObj(json, new TypeReference<Map<String, Object>>() {
		});
	}

	default SecurityScope toScope(AuthorizationVO vo) {
		return SecurityMapstruct.INSTANCE.ofVo(vo);
	}

}
