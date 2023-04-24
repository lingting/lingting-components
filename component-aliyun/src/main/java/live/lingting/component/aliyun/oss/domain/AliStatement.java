package live.lingting.component.aliyun.oss.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

/**
 * @author lingting 2023-04-21 14:41
 */
@Data
public class AliStatement {

	@JsonProperty("Effect")
	private String effect;

	@JsonProperty("Action")
	private Set<String> actions;

	@JsonProperty("Resource")
	private Set<String> resources;

}
