package com.silenteight.warehouse.indexer.query.index;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QueryIndexConfiguration {

  @Bean
  QueryIndexService indexService(OpendistroElasticClient opendistroElasticClient) {
    return new QueryIndexService(new SqlBuilder(), opendistroElasticClient);
  }
}
