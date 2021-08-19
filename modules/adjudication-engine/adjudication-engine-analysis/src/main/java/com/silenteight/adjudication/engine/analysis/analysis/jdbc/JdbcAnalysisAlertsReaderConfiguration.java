package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(JdbcAnalysisAlertsReaderProperties.class)
class JdbcAnalysisAlertsReaderConfiguration {

  @Valid
  private final JdbcAnalysisAlertsReaderProperties properties;

  @Bean
  JdbcAnalysisAlertsReader jdbcAnalysisDatasetAlertsReader(DataSource dataSource) {
    return new JdbcAnalysisAlertsReader(
        dataSource, properties.getChunkSize(), properties.getMaxRows());
  }
}
