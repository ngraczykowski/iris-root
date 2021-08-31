package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(JdbcDatasetAlertsAdderProperties.class)
class JdbcDatasetAlertsAdderConfiguration {

  @Valid
  private final JdbcDatasetAlertsAdderProperties properties;

  @Bean
  JdbcDatasetAlertsAdder jdbcAnalysisDatasetAlertsReader(DataSource dataSource) {
    return new JdbcDatasetAlertsAdder(
        dataSource, properties.getChunkSize(), properties.getMaxRows());
  }
}
