package live.lingting.component.security.mapstruct;

import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.vo.AuthorizationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author lingting 2023-03-30 13:55
 */
@Mapper
public interface SecurityMapstruct {

	SecurityMapstruct INSTANCE = Mappers.getMapper(SecurityMapstruct.class);

	AuthorizationVO toVo(SecurityScope scope);

	SecurityScope ofVo(AuthorizationVO vo);

}
