package live.lingting.component.core.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lingting
 */
@UtilityClass
public class HttpConstants {

	public static final String HTTP = "http";

	public static final String HTTPS = "https";

	public static final String URL_DELIMITER = "://";

	public static final String HOST_DELIMITER = "/";

	public static final char HOST_DELIMITER_CHAR = '/';

	public static final String QUERY_DELIMITER = "?";

	public static final String QUERY_DELIMITER_SPLIT = "\\?";

	public static final String QUERY_PARAMS_DELIMITER = "&";

	public static final String DISPOSITION_TEMPLATE = "Content-Disposition: form-data; name=\"%s\"\r\n\r\n";

	public static final String DISPOSITION_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\n";

	public static final String CONTENT_TYPE_TEMPLATE = "Content-Type: %s\r\n\r\n";

	public static final String MULTIPART_DEFAULT_CONTENT_TYPE = "application/octet-stream";

	public static final int SUCCESS_CODE = 200;

	public static final String GZIP = "gzip";

	public static final String DEFLATE = "deflate";

	public static final String UA = "lingting";

	public static final String HEADER_CONTENT_TYPE_CHARSET = "%s; charset=%s";

	public static final String HEADER_CONTENT_TYPE_BOUNDARY = "%s; charset=%s; boundary=%s";

	public static final String CONNECTION_CLOSE = "close";

	public static final String SSL = "SSL";

	public static final String SSL_V2 = "SSLv2";

	public static final String SSL_V3 = "SSLv3";

	public static final String TLS = "TLS";

	public static final String TLS_V1 = "TLSv1";

	public static final String TLS_V11 = "TLSv1.1";

	public static final String TLS_V12 = "TLSv1.2";

	public static final String HEADER_APPLICATION_JSON = "application/json";

	public static final String HEADER_APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

	public static final String HEADER_MULTIPART_FORM_DATA = "multipart/form-data";

	public static final String HEADER_APPLICATION_XML = "application/xml";

	public static final String HEADER_APPLICATION_PDF = "application/pdf";

	public static final String HEADER_APPLICATION_MSWORD = "application/msword";

	public static final String HEADER_APPLICATION_OCTET_STREAM = "application/octet-stream";

	public static final String HEADER_TEXT_HTML = "text/html";

	public static final String HEADER_TEXT_PLAIN = "text/plain";

	public static final String HEADER_TEXT_XML = "text/xml";

	public static final String HEADER_IMAGE_GIF = "image/gif";

	public static final String HEADER_IMAGE_JPEG = "image/jpeg";

	public static final String HEADER_IMAGE_PNG = "image/png";

	public static final String HEADER_HOST = "Host";

	public static final String HEADER_ORIGIN = "Origin";

	public static final String HEADER_ACCEPT = "Accept";

	public static final String HEADER_USER_AGENT = "User-Agent";

	public static final String HEADER_CONTENT_TYPE = "Content-Type";

	public static final String HEADER_CONTENT_LENGTH = "Content-Length";

	public static final String HEADER_AUTHORIZATION = "Authorization";

	public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";

	public static final String HEADER_CONNECTION = "Connection";

	public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";

	public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

	public static final String HEADER_REFERER = "Referer";

	public static final String HEADER_UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";

	public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";

	public static final String HEADER_IF_NONE_MATCH = "If-None-Match";

	public static final String HEADER_CACHE_CONTROL = "Cache-Control";

}
