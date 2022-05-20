package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.indexer.alert.AlertRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GroupingQueryConfiguration {

  @Bean
  @Qualifier("postgres")
  GroupingQueryService customPostgresReportingService(AlertRepository alertRepository) {
    return new GroupingQueryPostgresService(alertRepository);
  }
}
