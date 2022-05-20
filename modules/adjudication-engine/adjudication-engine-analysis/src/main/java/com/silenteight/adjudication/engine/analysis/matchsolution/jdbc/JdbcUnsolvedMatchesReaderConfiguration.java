package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(UnsolvedMatchesReaderProperties.class)
class JdbcUnsolvedMatchesReaderConfiguration {

  @Valid
  private final UnsolvedMatchesReaderProperties properties;

  @Bean
  JdbcUnsolvedMatchesReader jdbcUnsolvedMatchesReader(DataSource dataSource) {
    return new JdbcUnsolvedMatchesReader(
        dataSource, properties.getChunkSize(), properties.getMaxRows());
  }
}
