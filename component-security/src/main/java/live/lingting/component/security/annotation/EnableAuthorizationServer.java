package live.lingting.component.security.annotation;

import live.lingting.component.security.configuration.SecurityAuthorizationConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2023-03-29 21:08
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(SecurityAuthorizationConfiguration.class)
public @interface EnableAuthorizationServer {

}
