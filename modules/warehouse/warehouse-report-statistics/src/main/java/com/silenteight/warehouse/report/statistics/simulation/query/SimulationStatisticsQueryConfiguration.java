package com.silenteight.warehouse.report.statistics.simulation.query;

import com.silenteight.warehouse.report.reporting.ReportProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ReportProperties.class)
class SimulationStatisticsQueryConfiguration {

  @Bean
  SimulationStatisticsQuery statisticsQuery(
      JdbcTemplate jdbcTemplate,
      @Valid ReportProperties properties) {

    return new SimulationStatisticsQuery(jdbcTemplate, properties.getStatistics());
  }
}
