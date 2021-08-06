package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
class UnsolvedMatchesDataAccessConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  InsertMatchSolutionQuery insertMatchSolutionQuery() {
    return new InsertMatchSolutionQuery(jdbcTemplate);
  }
}
