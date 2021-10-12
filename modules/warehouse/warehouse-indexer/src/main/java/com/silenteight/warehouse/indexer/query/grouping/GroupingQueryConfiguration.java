package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.query.index.FieldsQueryIndexService;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
class GroupingQueryConfiguration {

  @Bean
  GroupingQueryService customReportingService(
      OpendistroElasticClient opendistroElasticClient,
      FieldsQueryIndexService fieldsQueryIndexService) {

    return new GroupingQueryService(
        new SqlBuilder(), opendistroElasticClient, fieldsQueryIndexService);
  }
}
