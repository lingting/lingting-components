package live.lingting.component.okhttp.enums;

import live.lingting.component.core.constant.HttpConstants;
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
	ACCEPT(HttpConstants.HEADER_ACCEPT),
	/**
	 * User_Agent
	 */
	USER_AGENT(HttpConstants.HEADER_USER_AGENT),
	/**
	 * Content_Type
	 */
	CONTENT_TYPE(HttpConstants.HEADER_CONTENT_TYPE),
	/**
	 * Content_Length
	 */
	CONTENT_LENGTH(HttpConstants.HEADER_CONTENT_LENGTH),
	/**
	 * Authorization
	 */
	AUTHORIZATION(HttpConstants.HEADER_AUTHORIZATION),
	/**
	 * Content-Encoding
	 */
	CONTENT_ENCODING(HttpConstants.HEADER_CONTENT_ENCODING),
	/**
	 * Connection
	 */
	CONNECTION(HttpConstants.HEADER_CONNECTION),
	/**
	 * Accept-Language
	 */
	ACCEPT_LANGUAGE(HttpConstants.HEADER_ACCEPT_LANGUAGE),
	/**
	 * Accept-Encoding
	 */
	ACCEPT_ENCODING(HttpConstants.HEADER_ACCEPT_ENCODING),
	/**
	 * Referer
	 */
	REFERER(HttpConstants.HEADER_REFERER),
	/**
	 * Upgrade-Insecure-Requests
	 */
	UPGRADE_INSECURE_REQUESTS(HttpConstants.HEADER_UPGRADE_INSECURE_REQUESTS),
	/**
	 * If-Modified-Since
	 */
	IF_MODIFIED_SINCE(HttpConstants.HEADER_IF_MODIFIED_SINCE),
	/**
	 * If-None-Match
	 */
	IF_NONE_MATCH(HttpConstants.HEADER_IF_NONE_MATCH),
	/**
	 * Cache-Control
	 */
	CACHE_CONTROL(HttpConstants.HEADER_CACHE_CONTROL),

	;

	private final String val;

}
