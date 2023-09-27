package live.lingting.component.dingtalk;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.dingtalk.message.DingTalkMessage;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.okhttp.enums.ContentType;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import okhttp3.RequestBody;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Supplier;

/**
 * 订单消息发送
 *
 * @author lingting 2020/6/10 21:25
 */
@Getter
@Accessors(chain = true)
public class DingTalkSender {

	private static final OkHttpClient HTTP_CLIENT = OkHttpClient.builder().build();

	/**
	 * 请求路径
	 */
	private final String url;

	private final Mac mac;

	/**
	 * 密钥
	 */
	private String secret;

	@Setter
	private Supplier<Long> currentTimeMillisSupplier = System::currentTimeMillis;

	@SneakyThrows(NoSuchAlgorithmException.class)
	public DingTalkSender(String url) {
		this.url = url;
		mac = Mac.getInstance("HmacSHA256");
	}

	/**
	 * 发送消息 根据参数值判断使用哪种发送方式
	 *
	 */
	public DingTalkResponse sendMessage(DingTalkMessage message) {
		if (StringUtils.hasText(getSecret())) {
			return sendSecretMessage(message);
		}
		else {
			return sendNormalMessage(message);
		}
	}

	/**
	 * 未使用 加签 安全设置 直接发送
	 */
	public DingTalkResponse sendNormalMessage(DingTalkMessage message) {
		return request(message, false);
	}

	/**
	 * 使用 加签 安全设置 发送
	 */
	public DingTalkResponse sendSecretMessage(DingTalkMessage message) {
		return request(message, true);
	}

	/**
	 * 设置密钥
	 */
	@SneakyThrows(InvalidKeyException.class)
	public DingTalkSender setSecret(String secret) {
		if (StringUtils.hasText(secret)) {
			this.secret = secret;
			mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		}
		return this;
	}

	/**
	 * 获取签名后的请求路径
	 * @param timestamp 当前时间戳
	 */
	@SneakyThrows
	public String secret(long timestamp) {
		byte[] secretBytes = (timestamp + "\n" + secret).getBytes(StandardCharsets.UTF_8);
		String secretBase64 = Base64.getEncoder().encodeToString(mac.doFinal(secretBytes));
		String sign = URLEncoder.encode(secretBase64, "utf-8");
		return String.format("%s&timestamp=%s&sign=%s", url, timestamp, sign);
	}

	/**
	 * 发起消息请求
	 * @param dingTalkMessage 消息内容
	 * @param isSecret 是否签名 true 签名
	 * @return java.lang.String
	 */
	public DingTalkResponse request(DingTalkMessage dingTalkMessage, boolean isSecret) {
		String message = dingTalkMessage.generate();
		Long timestamp = getCurrentTimeMillisSupplier().get();

		String requestUrl = isSecret ? secret(timestamp) : getUrl();
		RequestBody requestBody = RequestBody.create(message, ContentType.APPLICATION_JSON.media());

		String response = HTTP_CLIENT.post(requestUrl, requestBody, String.class);
		return DingTalkResponse.of(response);
	}

}
