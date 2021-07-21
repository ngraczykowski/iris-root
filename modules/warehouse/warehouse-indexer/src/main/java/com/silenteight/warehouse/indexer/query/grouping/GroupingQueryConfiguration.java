package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.indexer.alert.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.query.SqlBuilder;
import com.silenteight.warehouse.indexer.query.index.QueryIndexService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ ElasticsearchProperties.class })
class GroupingQueryConfiguration {

  @Bean
  GroupingQueryService customReportingService(
      OpendistroElasticClient opendistroElasticClient,
      QueryIndexService queryIndexService) {

    return new GroupingQueryService(new SqlBuilder(), opendistroElasticClient, queryIndexService);
  }
}
