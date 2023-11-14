package live.lingting.component.aliyun.domain;

import lombok.Data;
import org.springframework.util.unit.DataSize;

/**
 * @author lingting 2023-11-14 11:52
 */
@Data
public class AliOssMeta {

	private DataSize size;

	private String versionId;

}
