package live.lingting.component.aliyun.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

/**
 * @author lingting 2023-04-21 14:41
 */
@Data
public class AliStsStatement {

	@JsonProperty("Effect")
	private String effect;

	@JsonProperty("Action")
	private Collection<String> actions;

	@JsonProperty("Resource")
	private Collection<String> resources;

}
