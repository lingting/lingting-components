package live.lingting.component.aliyun.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

/**
 * @author lingting 2023-04-21 14:40
 */
@Data
public class AliStsPolicy {

	@JsonProperty("Version")
	private String version = "1";

	@JsonProperty("Statement")
	private Collection<AliStsStatement> statements;

	/**
	 * sts过期时间, 单位: 秒
	 */
	@JsonIgnore
	private Long durationSeconds;

}
