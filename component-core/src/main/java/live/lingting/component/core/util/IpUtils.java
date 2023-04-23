package live.lingting.component.core.util;

import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author psh 2022-04-21 16:55
 */
@Slf4j
@UtilityClass
public class IpUtils {

	public static final String LOCALHOST = "127.0.0.1";

	public static final String UNKNOWN = "UNKNOWN";

	public static final String IP_SPLIT = ",";

	public static final Integer IPV4_LENGTH = 16;

	private static final List<String> HEADERS;

	static {
		HEADERS = new ArrayList<>(16);
		HEADERS.add("X-Forwarded-For");
		HEADERS.add("Node-Forwarded-IP");
		HEADERS.add("X-Real-Ip");
		HEADERS.add("Proxy-Client-IP");
		HEADERS.add("WL-Proxy-Client-IP");
		HEADERS.add("HTTP_CLIENT_IP");
		HEADERS.add("HTTP_X_FORWARDED_FOR");
	}

	public static String getFirstIp(HttpServletRequest request) {
		String ip;
		for (String header : HEADERS) {
			// ip 通过校验
			ip = validIp(request.getHeader(header));
			if (ip != null) {
				return ip;
			}
		}

		return validIp(request.getRemoteAddr());
	}

	public static String validIp(String originIp) {
		if (isLocalhost(originIp)) {
			return LOCALHOST;
		}

		if (!StringUtils.hasText(originIp) || UNKNOWN.equalsIgnoreCase(originIp)) {
			return null;
		}

		if (originIp.contains(IP_SPLIT)) {
			originIp = originIp.substring(0, originIp.indexOf(IP_SPLIT));
		}

		if (originIp.length() >= IPV4_LENGTH) {
			originIp = originIp.substring(0, IPV4_LENGTH);
		}

		return originIp;
	}

	public static boolean isLocalhost(String ip) {
		if (!StringUtils.hasText(ip)) {
			return false;
		}
		switch (ip) {
			case "[0:0:0:0:0:0:0:1]":
			case "0:0:0:0:0:0:0:1":
			case LOCALHOST:
			case "localhost":
				return true;
			default:
				return false;
		}
	}

	public static List<String> list(HttpServletRequest request) {
		List<String> list = new ArrayList<>();

		for (String header : HEADERS) {
			String val = request.getHeader(header);
			if (StringUtils.hasText(val) && !UNKNOWN.equalsIgnoreCase(val)) {
				if (val.contains(IP_SPLIT)) {
					list.addAll(Arrays.asList(val.split(IP_SPLIT)));
				}
				else {
					list.add(val);
				}
			}
		}

		list.add(request.getRemoteAddr());
		return list;
	}

}
