package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
public class FeatureDataAccessConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  InsertFeatureQuery insertMatchSolutionQuery() {
    return new InsertFeatureQuery(jdbcTemplate);
  }
}
