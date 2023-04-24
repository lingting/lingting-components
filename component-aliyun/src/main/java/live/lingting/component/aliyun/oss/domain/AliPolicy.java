package live.lingting.component.aliyun.oss.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

/**
 * @author lingting 2023-04-21 14:40
 */
@Data
public class AliPolicy {

	@JsonProperty("Version")
	private String version = "1";

	@JsonProperty("Statement")
	private Collection<AliStatement> statements;

}
