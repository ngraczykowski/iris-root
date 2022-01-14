package com.silenteight.warehouse.indexer.query.single;

import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMappingProperties;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({
    ElasticsearchProperties.class,
    AlertMappingProperties.class
})
public class SingleAlertQueryConfiguration {

  @Bean
  AlertSearchService searchAlertService() {
    return new AlertSearchService();
  }

  @Bean
  ProductionSearchRequestBuilder productionSearchRequestBuilder(
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new ProductionSearchRequestBuilder(new String[] {
        elasticsearchProperties.getProductionQueryIndex() });
  }

  @Bean
  ProductionSearchRequestBuilder productionMatchSearchRequestBuilder(
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new ProductionSearchRequestBuilder(new String[] {
        elasticsearchProperties.getProductionMatchQueryIndex() });
  }

  @Bean
  AlertQueryService alertQueryService(
      RestHighLevelClient restHighLevelUserAwareClient, AlertSearchService alertSearchService,
      ProductionSearchRequestBuilder productionSearchRequestBuilder) {

    return new AlertQueryService(restHighLevelUserAwareClient, alertSearchService,
        productionSearchRequestBuilder);
  }

  @Bean
  @ConditionalOnProperty(value="isSqlSupported", havingValue = "false", matchIfMissing = true)
  // TODO(tdrozdz): Add isSqlSupported to properties
  RandomAlertService randomElasticAlertQueryService(
      AlertSearchService alertSearchService, RestHighLevelClient restHighLevelAdminClient,
      ProductionSearchRequestBuilder productionSearchRequestBuilder) {

    return new RandomElasticSearchAlertQueryService(alertSearchService, restHighLevelAdminClient,
        productionSearchRequestBuilder);
  }

  @Bean
  @ConditionalOnProperty(value="isSqlSupported", havingValue = "true")
    // TODO(tdrozdz): Add isSqlSupported to properties
  RandomAlertService randomPostgresAlertQueryService() {
    return new RandomPostgresSearchAlertQueryService();
  }
}
