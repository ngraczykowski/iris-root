package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.indexer.alert.indexing.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.query.index.FieldsQueryIndexService;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
class GroupingQueryConfiguration {

  @Bean
  @Primary
  GroupingQueryService customElasticSearchReportingService(
      OpendistroElasticClient opendistroElasticClient,
      FieldsQueryIndexService fieldsQueryIndexService) {

    return new GroupingQueryElasticSearchService(
        new SqlBuilder(), opendistroElasticClient, fieldsQueryIndexService);
  }

  @Bean
  @Qualifier("postgres")
  GroupingQueryService customPostgresReportingService() {
    return new GroupingQueryPostgresService(null);
  }
}
