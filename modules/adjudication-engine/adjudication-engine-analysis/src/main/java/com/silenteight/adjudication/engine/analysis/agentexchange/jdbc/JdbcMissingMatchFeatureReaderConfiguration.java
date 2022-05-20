package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(MissingMatchFeatureReaderProperties.class)
class JdbcMissingMatchFeatureReaderConfiguration {

  @Valid
  private final MissingMatchFeatureReaderProperties properties;

  @Bean
  JdbcMissingMatchFeatureReader jdbcMissingMatchFeatureReader(DataSource dataSource) {
    return new JdbcMissingMatchFeatureReader(
        dataSource, properties.getChunkSize(), properties.getMaxRows());
  }
}
