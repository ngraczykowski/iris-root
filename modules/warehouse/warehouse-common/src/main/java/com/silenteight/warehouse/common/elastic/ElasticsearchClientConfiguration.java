package com.silenteight.warehouse.common.elastic;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthContributorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchRestHealthIndicator;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.CustomElasticsearchRestClientConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static java.util.Map.of;

@Configuration
@ImportAutoConfiguration(exclude = {
    ElasticsearchRestClientAutoConfiguration.class,
    ElasticsearchDataAutoConfiguration.class
})
@EnableConfigurationProperties(ElasticsearchRestClientProperties.class)
@Import(CustomElasticsearchRestClientConfiguration.class)
public class ElasticsearchClientConfiguration extends
    CompositeHealthContributorConfiguration<ElasticsearchRestHealthIndicator, RestClient> {

  @Bean
  RestHighLevelElasticClientFactory restHighLevelElasticClientFactory(
      RestClientBuilder restClientBuilder,
      ElasticsearchRestClientProperties properties,
      UserAwareTokenProvider userAwareTokenProvider) {

    return new RestHighLevelElasticClientFactory(
        restClientBuilder, properties, userAwareTokenProvider);
  }

  @Bean
  RestHighLevelClient restHighLevelAdminClient(
      RestHighLevelElasticClientFactory restHighLevelElasticClientFactory) {

    return restHighLevelElasticClientFactory.getAdminClient();
  }

  @Bean
  RestHighLevelClient restHighLevelUserAwareClient(
      RestHighLevelElasticClientFactory restHighLevelElasticClientFactory) {

    return restHighLevelElasticClientFactory.getUserAwareClient();
  }

  @ConditionalOnEnabledHealthIndicator(value = "elasticsearch")
  @Bean
  @ConditionalOnMissingBean(name = {
      "elasticsearchHealthIndicator", "elasticsearchHealthContributor" })
  public HealthContributor elasticsearchHealthContributor(
      RestHighLevelClient restHighLevelAdminClient) {

    return createContributor(
        of("restHighLevelAdminClient", restHighLevelAdminClient.getLowLevelClient()));
  }
}
