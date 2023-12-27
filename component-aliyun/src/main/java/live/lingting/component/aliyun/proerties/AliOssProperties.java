package live.lingting.component.aliyun.proerties;

import live.lingting.component.aliyun.domain.AliOssCredentials;
import live.lingting.component.aliyun.enums.AliOssAcl;
import live.lingting.component.aliyun.mapstruct.AliMapstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2023-04-21 14:18
 */
@Getter
@Setter
@ConfigurationProperties(AliOssProperties.PREFIX)
public class AliOssProperties extends AbstractProperties {

	public static final String PREFIX = "lingting.aliyun.oss";

	private String bucket;

	private AliOssAcl acl = AliOssAcl.DEFAULT;

	public AliOssProperties using(AliOssCredentials credentials) {
		AliOssProperties properties = AliMapstruct.INSTANCE.ofCredentials(credentials);
		properties.setAcl(getAcl());
		return properties;
	}

}
