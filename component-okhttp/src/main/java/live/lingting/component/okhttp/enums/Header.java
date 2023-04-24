package live.lingting.component.okhttp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/7/12 16:39
 */
@Getter
@AllArgsConstructor
public enum Header {

	/**
	 * Accept
	 */
	ACCEPT("Accept"),
	/**
	 * User_Agent
	 */
	USER_AGENT("User-Agent"),
	/**
	 * Content_Type
	 */
	CONTENT_TYPE("Content-Type"),
	/**
	 * Content_Length
	 */
	CONTENT_LENGTH("Content-Length"),
	/**
	 * Authorization
	 */
	AUTHORIZATION("Authorization"),
	/**
	 * Content-Encoding
	 */
	CONTENT_ENCODING("Content-Encoding"),
	/**
	 * Connection
	 */
	CONNECTION("Connection"),
	/**
	 * Accept-Language
	 */
	ACCEPT_LANGUAGE("Accept-Language"),
	/**
	 * Accept-Encoding
	 */
	ACCEPT_ENCODING("Accept-Encoding"),
	/**
	 * Referer
	 */
	REFERER("Referer"),
	/**
	 * Upgrade-Insecure-Requests
	 */
	UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
	/**
	 * If-Modified-Since
	 */
	IF_MODIFIED_SINCE("If-Modified-Since"),
	/**
	 * If-None-Match
	 */
	IF_NONE_MATCH("If-None-Match"),
	/**
	 * Cache-Control
	 */
	CACHE_CONTROL("Cache-Control"),

	;

	private final String val;

}
