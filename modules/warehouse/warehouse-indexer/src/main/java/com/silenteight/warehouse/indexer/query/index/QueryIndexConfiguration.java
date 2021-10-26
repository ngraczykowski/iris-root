package com.silenteight.warehouse.indexer.query.index;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class QueryIndexConfiguration {

  @Bean
  FieldsQueryIndexService fieldsQueryIndexService(
      OpendistroElasticClient opendistroElasticClient) {

    return new FieldsQueryIndexService(new SqlBuilder(), opendistroElasticClient);
  }

  @Bean
  ProductionIndexingQuery productionIndexingQuery(
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new ProductionIndexingQuery(elasticsearchProperties.getProductionQueryIndex());
  }

  @Bean
  ProductionIndexingQuery productionMatchIndexingQuery(
      @Valid ElasticsearchProperties elasticsearchProperties) {

    return new ProductionIndexingQuery(elasticsearchProperties.getProductionMatchQueryIndex());
  }

  @Bean
  SimulationIndexingQuery simulationIndexingQuery(
      @Valid EnvironmentProperties environmentProperties) {

    return new SimulationIndexingQuery(environmentProperties.getPrefix());
  }
}
