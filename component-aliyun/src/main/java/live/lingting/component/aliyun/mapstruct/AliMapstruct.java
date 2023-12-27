package live.lingting.component.aliyun.mapstruct;

import live.lingting.component.aliyun.domain.AliOssCredentials;
import live.lingting.component.aliyun.proerties.AliOssProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author lingting 2023-12-26 21:34
 */
@Mapper
public interface AliMapstruct {

	AliMapstruct INSTANCE = Mappers.getMapper(AliMapstruct.class);

	@Mapping(source = "credentials.accessKeyId", target = "accessKey")
	@Mapping(source = "credentials.accessKeySecret", target = "accessSecret")
	AliOssProperties ofCredentials(AliOssCredentials credentials);

}
