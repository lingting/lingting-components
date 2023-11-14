package live.lingting.component.aliyun;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectAcl;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import live.lingting.component.aliyun.domain.AliOssCredentials;
import live.lingting.component.aliyun.domain.AliOssMeta;
import live.lingting.component.aliyun.domain.AliOssOwnerAcl;
import live.lingting.component.aliyun.enums.AliOssAcl;
import live.lingting.component.aliyun.oss.AliOssBucket;
import live.lingting.component.aliyun.oss.AliOssObject;
import live.lingting.component.aliyun.proerties.AliOssProperties;
import lombok.Getter;
import org.springframework.util.unit.DataSize;

import java.io.InputStream;

/**
 * @author lingting 2023-04-19 10:47
 */
@Getter
public class AliOss {

	private final OSSClient client;

	private final AliOssProperties properties;

	private final String region;

	private final String accessKey;

	private final String accessSecret;

	private final String securityToken;

	protected AliOss(AliOssProperties properties, OSSClientBuilder.OSSClientBuilderImpl builder) {
		String endpoint = String.format("oss-%s.aliyuncs.com", properties.getRegion());
		builder.endpoint(endpoint).region(properties.getRegion());

		this.client = (OSSClient) builder.build();
		this.properties = properties;
		this.region = properties.getRegion();
		this.accessKey = properties.getAccessKey();
		this.accessSecret = properties.getAccessSecret();

		Credentials credentials = client.getCredentialsProvider().getCredentials();
		this.securityToken = credentials.getSecurityToken();
	}

	public AliOss(AliOssProperties properties) {
		this(properties, of(properties));
	}

	public AliOss(AliOssProperties properties, AliOssCredentials credentials) {
		this(properties, of(credentials));
	}

	static OSSClientBuilder.OSSClientBuilderImpl of(AliOssProperties properties) {
		DefaultCredentialProvider credentialsProvider = new DefaultCredentialProvider(properties.getAccessKey(),
				properties.getAccessSecret());

		ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
		configuration.setProtocol(Protocol.HTTPS);

		return OSSClientBuilder.create().credentialsProvider(credentialsProvider).clientConfiguration(configuration);
	}

	static OSSClientBuilder.OSSClientBuilderImpl of(AliOssCredentials credentials) {
		DefaultCredentialProvider credentialsProvider = new DefaultCredentialProvider(credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
		configuration.setProtocol(Protocol.HTTPS);

		return OSSClientBuilder.create().credentialsProvider(credentialsProvider).clientConfiguration(configuration);
	}

	public AliOssBucket bucket(String bucket) {
		return new AliOssBucket(this, bucket);
	}

	public AliOssBucket bucket(AliOssCredentials credentials) {
		return new AliOssBucket(properties, credentials);
	}

	public AliOssObject object(String objectKey) {
		return object(properties.getBucket(), objectKey);
	}

	public AliOssObject object(String bucket, String objectKey) {
		return new AliOssObject(this, bucket, objectKey);
	}

	public AliOssObject object(AliOssCredentials credentials) {
		return new AliOssObject(properties, credentials);
	}

	// region bucket
	// endregion

	// region object
	static ObjectMetadata fillAcl(PutObjectRequest request, AliOssAcl acl) {
		ObjectMetadata metadata = request.getMetadata() == null ? new ObjectMetadata() : request.getMetadata();

		// 不为空或者不是默认acl 才进行acl设置
		if (acl != null && !AliOssAcl.DEFAULT.equals(acl)) {
			CannedAccessControlList parse = CannedAccessControlList.parse(acl.getValue());
			metadata.setObjectAcl(parse);
		}

		request.setMetadata(metadata);
		return metadata;
	}

	public InputStream objectGet(String bucket, String key) {
		OSSObject object = client.getObject(bucket, key);
		return object.getObjectContent();
	}

	public AliOssOwnerAcl objectGetOwnerAcl(String bucket, String key) {
		ObjectAcl objectAcl = client.getObjectAcl(bucket, key);

		AliOssOwnerAcl ownerAcl = new AliOssOwnerAcl();
		ownerAcl.setOwner(objectAcl.getOwner());

		String string = objectAcl.getPermission().toString();
		ownerAcl.setAcl(AliOssAcl.of(string));
		return ownerAcl;
	}

	public AliOssMeta objectGetMeta(String bucket, String key) {
		ObjectMetadata metadata = client.getObjectMetadata(bucket, key);

		AliOssMeta meta = new AliOssMeta();
		meta.setSize(DataSize.ofBytes(metadata.getContentLength()));
		meta.setVersionId(metadata.getVersionId());
		return meta;
	}

	/**
	 * 推送文件
	 * @param bucket 桶
	 * @param key 对象key
	 * @param in 对象内容流
	 * @return java.lang.String 版本id
	 */
	public String objectPut(String bucket, String key, InputStream in) {
		return objectPut(bucket, key, in, properties.getAcl());
	}

	public String objectPut(String bucket, String key, InputStream in, AliOssAcl acl) {
		PutObjectRequest request = new PutObjectRequest(bucket, key, in);

		fillAcl(request, acl);

		PutObjectResult response = client.putObject(request);
		return response.getVersionId();
	}

	public Long objectAppend(String bucket, String key, InputStream in) {
		AliOssMeta meta = objectGetMeta(bucket, key);
		DataSize size = meta.getSize();
		long position = size.toBytes();
		return objectAppend(bucket, key, position, in);
	}

	public Long objectAppend(String bucket, String key, Long position, InputStream in) {
		return objectAppend(bucket, key, position, in, properties.getAcl());
	}

	public Long objectAppend(String bucket, String key, Long position, InputStream in, AliOssAcl acl) {
		AppendObjectRequest request = new AppendObjectRequest(bucket, key, in);
		request.setPosition(position);

		fillAcl(request, acl);

		AppendObjectResult response = client.appendObject(request);
		return response.getNextPosition();
	}

	// endregion

}
