package live.lingting.component.validation.group;

import javax.validation.groups.Default;

/**
 * 校验分组用 需要继承默认分组 {@link Default} 否则，如果设定校验分组为当前类时，不会去校验没有设置分组的规则
 *
 * @author lingting 2020-08-20 10:10
 */
public interface CreateGroup extends Default {

}
