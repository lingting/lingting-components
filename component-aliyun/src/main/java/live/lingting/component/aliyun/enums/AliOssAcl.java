package live.lingting.component.aliyun.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2023-11-13 19:07
 */
@Getter
@AllArgsConstructor
public enum AliOssAcl {

	/** private */
	PRIVATE("private"),
	/** public-read */
	PUBLIC_READ("public-read"),
	/** public-read-write */
	PUBLIC_READ_WRITE("public-read-write"),
	/** default */
	DEFAULT("default"),

	;

	private final String value;

	@JsonCreator
	public static AliOssAcl of(String s) {
		for (AliOssAcl e : values()) {
			if (e.getValue().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}

}
