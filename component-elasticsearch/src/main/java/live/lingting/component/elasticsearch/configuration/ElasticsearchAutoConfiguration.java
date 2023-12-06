package live.lingting.component.elasticsearch.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.lingting.component.elasticsearch.datascope.DataPermissionHandler;
import live.lingting.component.elasticsearch.datascope.DataScope;
import live.lingting.component.elasticsearch.datascope.DefaultDataPermissionHandler;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2023-06-06 14:51
 */
@AutoConfiguration
@RequiredArgsConstructor
public class ElasticsearchAutoConfiguration {

	@Bean
	@ConditionalOnBean(ObjectMapper.class)
	public JsonpMapper jsonpMapper(ObjectMapper mapper) {
		return new JacksonJsonpMapper(mapper);
	}

	@Bean
	public RestClientBuilderCustomizer restClientBuilderCustomizer() {
		return builder -> builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
			.setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(true).build()));
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean({ RestClient.class, JsonpMapper.class })
	public ElasticsearchTransport restClientTransport(RestClient restClient, JsonpMapper jsonpMapper) {
		return new RestClientTransport(restClient, jsonpMapper);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(ElasticsearchClient.class)
	public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
		return new ElasticsearchClient(transport);
	}

	@Bean
	@ConditionalOnMissingBean
	public DataPermissionHandler elasticsearchDataPermissionHandler(List<DataScope> scopes) {
		return new DefaultDataPermissionHandler(scopes);
	}

}
