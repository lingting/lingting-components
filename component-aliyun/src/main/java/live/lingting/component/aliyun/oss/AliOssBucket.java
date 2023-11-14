package live.lingting.component.aliyun.oss;

import live.lingting.component.aliyun.AliOss;
import live.lingting.component.aliyun.domain.AliOssCredentials;
import live.lingting.component.aliyun.proerties.AliOssProperties;
import lombok.Getter;

/**
 * @author lingting 2023-11-13 15:50
 */
@Getter
public class AliOssBucket {

	private final AliOss oss;

	private final String bucket;

	public AliOssBucket(AliOss oss, String bucket) {
		this.oss = oss;
		this.bucket = bucket;
	}

	public AliOssBucket(AliOssProperties properties, AliOssCredentials credentials) {
		this(new AliOss(properties, credentials), credentials.getBucket());
	}

}
