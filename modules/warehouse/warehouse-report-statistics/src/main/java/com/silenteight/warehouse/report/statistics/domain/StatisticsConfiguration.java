package com.silenteight.warehouse.report.statistics.domain;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService;
import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ReportProperties.class)
class StatisticsConfiguration {

  @Bean
  @ConditionalOnProperty("warehouse.report.statistics")
  StatisticsQuery statisticsQuery(
      GroupingQueryService groupingQueryService,
      @Qualifier(value = "simulationIndexingQuery") IndexesQuery indexerQuery,
      @Valid ReportProperties statisticsProperties) {

    return new StatisticsQuery(groupingQueryService, indexerQuery,
        statisticsProperties.getStatistics());
  }
}
