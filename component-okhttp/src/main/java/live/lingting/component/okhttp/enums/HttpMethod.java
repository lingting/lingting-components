package live.lingting.component.okhttp.enums;

/**
 * @author lingting 2021/9/1 14:32
 */
public enum HttpMethod {

	/**
	 * GET
	 */
	GET,
	/**
	 * POST
	 */
	POST,
	/**
	 * PATCH
	 */
	PATCH,
	/**
	 * PUT
	 */
	PUT,
	/**
	 * DELETE
	 */
	DELETE,
	/**
	 * MOVE
	 */
	MOVE,
	/**
	 * PROPPATCH
	 */
	PROPPATCH,
	/**
	 * REPORT
	 */
	REPORT,
	/**
	 * HEAD
	 */
	HEAD,

	;

	public static HttpMethod of(String value) {
		for (HttpMethod e : values()) {
			if (e.name().equals(value)) {
				return e;
			}
		}
		return null;
	}

}
