package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
class JdbcAgentExchangeConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  DeleteAgentExchangeMatchFeatureQuery deleteAgentExchangeMatchFeatureQuery() {
    return new DeleteAgentExchangeMatchFeatureQuery(jdbcTemplate);
  }
}
