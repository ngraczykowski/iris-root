package com.silenteight.warehouse.report.statistics.domain;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(StatisticsProperties.class)
class StatisticsConfiguration {

  @Bean
  StatisticsQuery statisticsQuery(
      GroupingQueryService groupingQueryService,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery indexerQuery,
      @Valid StatisticsProperties statisticsProperties) {

    return new StatisticsQuery(groupingQueryService, indexerQuery,
        statisticsProperties);
  }
}
