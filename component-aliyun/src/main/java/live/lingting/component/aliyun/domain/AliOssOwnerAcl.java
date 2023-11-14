package live.lingting.component.aliyun.domain;

import com.aliyun.oss.model.Owner;
import live.lingting.component.aliyun.enums.AliOssAcl;
import lombok.Data;

/**
 * @author lingting 2023-11-13 19:12
 */
@Data
public class AliOssOwnerAcl {

	private Owner owner;

	private AliOssAcl acl;

}
