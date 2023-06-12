package live.lingting.component.web.configuration;

import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.servlet.api.Deployment;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.spec.ServletContextImpl;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import live.lingting.component.core.thread.AbstractTimer;
import live.lingting.component.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import java.io.File;

/**
 * @author lingting 2023-06-09 17:52
 */
@AutoConfiguration
@ConditionalOnClass(Undertow.class)
public class ComponentUndertowAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WebServerFactoryCustomizer<UndertowServletWebServerFactory> componentUndertowServletWebServerFactoryCustomization() {
		return factory -> factory.addDeploymentInfoCustomizers(deploymentInfo -> {
			WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
			webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 2048, -1, 24, 0));
			deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo",
					webSocketDeploymentInfo);
		});
	}

	/**
	 * 定时创建 undertow 的用来上传文件的临时文件夹, 避免文件上传异常 /tmp/undertow.35301.2529636817692511076
	 */
	@Bean
	@ConditionalOnMissingBean
	public ComponentUndertowTimer componentUndertowTimer(ServletContext context) {
		File dir = null;
		if (context instanceof ServletContextImpl) {
			Deployment deployment = ((ServletContextImpl) context).getDeployment();
			DeploymentInfo deploymentInfo = deployment.getDeploymentInfo();
			MultipartConfigElement config = deploymentInfo.getDefaultMultipartConfig();
			if (config != null && StringUtils.hasText(config.getLocation())) {
				dir = new File(config.getLocation());
			}
			else {
				dir = deploymentInfo.getTempDir();
			}
		}

		return new ComponentUndertowTimer(dir);
	}

	@RequiredArgsConstructor
	public static class ComponentUndertowTimer extends AbstractTimer {

		protected final File dir;

		@Override
		protected void process() throws Exception {
			try {
				if (dir == null || dir.exists()) {
					return;
				}
				dir.mkdirs();
			}
			catch (Exception e) {
				//
			}
		}

	}

}
