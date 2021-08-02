package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.indexer.alert.ElasticsearchProperties;
import com.silenteight.warehouse.indexer.query.SqlBuilder;
import com.silenteight.warehouse.indexer.query.index.QueryIndexService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({ ElasticsearchProperties.class, DataProductionProperties.class })
class GroupingQueryConfiguration {

  @Bean
  SqlBuilder sqlBuilder(@Valid DataProductionProperties dataProductionProperties) {
    return new SqlBuilder(
        dataProductionProperties.getFieldName(), dataProductionProperties.getCompletedValue());
  }

  @Bean
  GroupingQueryService customReportingService(
      SqlBuilder sqlBuilder,
      OpendistroElasticClient opendistroElasticClient,
      QueryIndexService queryIndexService) {

    return new GroupingQueryService(sqlBuilder, opendistroElasticClient, queryIndexService);
  }
}
