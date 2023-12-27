package live.lingting.component.aliyun.oss;

import live.lingting.component.aliyun.AliOss;
import live.lingting.component.aliyun.domain.AliOssCredentials;
import live.lingting.component.aliyun.domain.AliOssMeta;
import live.lingting.component.aliyun.domain.AliOssOwnerAcl;
import live.lingting.component.aliyun.enums.AliOssAcl;
import live.lingting.component.aliyun.proerties.AliOssProperties;
import lombok.Getter;

import java.io.InputStream;

/**
 * @author lingting 2023-11-13 15:48
 */
@Getter
public class AliOssObject {

	private final AliOss oss;

	private final String bucket;

	private final String objectKey;

	public AliOssObject(AliOss oss, String bucket, String objectKey) {
		this.oss = oss;
		this.bucket = bucket;
		this.objectKey = objectKey;
	}

	public AliOssObject(AliOssProperties properties, AliOssCredentials credentials) {
		this(new AliOss(properties, credentials), credentials.getBucket(), credentials.getObjectKey());
	}

	public InputStream get() {
		return oss.objectGet(bucket, objectKey);
	}

	public AliOssOwnerAcl getOwnerAcl() {
		return oss.objectGetOwnerAcl(bucket, objectKey);
	}

	public AliOssMeta getMeta() {
		return oss.objectGetMeta(bucket, objectKey);
	}

	public String put(InputStream in) {
		return oss.objectPut(bucket, objectKey, in);
	}

	public String put(InputStream in, AliOssAcl acl) {
		return oss.objectPut(bucket, objectKey, in, acl);
	}

	public Long append(InputStream in) {
		return oss.objectAppend(bucket, objectKey, in);
	}

	public Long append(Long position, InputStream in) {
		return oss.objectAppend(bucket, objectKey, position, in);
	}

	public Long append(Long position, InputStream in, AliOssAcl acl) {
		return oss.objectAppend(bucket, objectKey, position, in, acl);
	}

	public String url() {
		return String.format("https://%s.%s/%s", bucket, oss.getEndpoint(), objectKey);
	}

}
