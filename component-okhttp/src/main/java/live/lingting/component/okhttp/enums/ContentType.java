package live.lingting.component.okhttp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.MediaType;

import static live.lingting.component.core.constant.HttpConstants.HEADER_APPLICATION_JSON;
import static live.lingting.component.core.constant.HttpConstants.HEADER_APPLICATION_MSWORD;
import static live.lingting.component.core.constant.HttpConstants.HEADER_APPLICATION_OCTET_STREAM;
import static live.lingting.component.core.constant.HttpConstants.HEADER_APPLICATION_PDF;
import static live.lingting.component.core.constant.HttpConstants.HEADER_APPLICATION_XML;
import static live.lingting.component.core.constant.HttpConstants.HEADER_APPLICATION_X_WWW_FORM_URLENCODED;
import static live.lingting.component.core.constant.HttpConstants.HEADER_IMAGE_GIF;
import static live.lingting.component.core.constant.HttpConstants.HEADER_IMAGE_JPEG;
import static live.lingting.component.core.constant.HttpConstants.HEADER_IMAGE_PNG;
import static live.lingting.component.core.constant.HttpConstants.HEADER_MULTIPART_FORM_DATA;
import static live.lingting.component.core.constant.HttpConstants.HEADER_TEXT_HTML;
import static live.lingting.component.core.constant.HttpConstants.HEADER_TEXT_PLAIN;
import static live.lingting.component.core.constant.HttpConstants.HEADER_TEXT_XML;

/**
 * @author lingting 2021/9/10 16:09
 */
@Getter
@AllArgsConstructor
public enum ContentType {

	/**
	 * json
	 */
	APPLICATION_JSON(HEADER_APPLICATION_JSON, MediaType.parse(HEADER_APPLICATION_JSON)),
	/**
	 * form
	 */
	APPLICATION_FORM_URLENCODED(HEADER_APPLICATION_X_WWW_FORM_URLENCODED,
			MediaType.parse(HEADER_APPLICATION_X_WWW_FORM_URLENCODED)),

	MULTIPART_FORM_DATA(HEADER_MULTIPART_FORM_DATA, MediaType.parse(HEADER_MULTIPART_FORM_DATA)),
	/**
	 * xml
	 */
	APPLICATION_XML(HEADER_APPLICATION_XML, MediaType.parse(HEADER_APPLICATION_XML)),
	/**
	 * application/pdf
	 */
	APPLICATION_PDF(HEADER_APPLICATION_PDF, MediaType.parse(HEADER_APPLICATION_PDF)),
	/**
	 * application/msword
	 */
	APPLICATION_MSWORD(HEADER_APPLICATION_MSWORD, MediaType.parse(HEADER_APPLICATION_MSWORD)),
	/**
	 * application/octet-stream 常用于文件下载
	 */
	APPLICATION_OCTET_STREAM(HEADER_APPLICATION_OCTET_STREAM, MediaType.parse(HEADER_APPLICATION_OCTET_STREAM)),
	/**
	 * text/html
	 */
	TEXT_HTML(HEADER_TEXT_HTML, MediaType.parse(HEADER_TEXT_HTML)),
	/**
	 * text/plain
	 */
	TEXT_PLAIN(HEADER_TEXT_PLAIN, MediaType.parse(HEADER_TEXT_PLAIN)),
	/**
	 * text/xml
	 */
	TEXT_XML(HEADER_TEXT_XML, MediaType.parse(HEADER_TEXT_XML)),
	/**
	 * image/gif
	 */
	IMAGE_GIF(HEADER_IMAGE_GIF, MediaType.parse(HEADER_IMAGE_GIF)),
	/**
	 * image/jpeg
	 */
	IMAGE_JPEG(HEADER_IMAGE_JPEG, MediaType.parse(HEADER_IMAGE_JPEG)),
	/**
	 * image/png
	 */
	IMAGE_PNG(HEADER_IMAGE_PNG, MediaType.parse(HEADER_IMAGE_PNG)),

	;

	private final String value;

	private final MediaType mediaType;

	public String value() {
		return getValue();
	}

	public MediaType media() {
		return getMediaType();
	}

}
