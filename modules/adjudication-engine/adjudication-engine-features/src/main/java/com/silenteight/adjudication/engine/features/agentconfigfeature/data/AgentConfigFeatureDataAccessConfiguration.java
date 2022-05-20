package com.silenteight.adjudication.engine.features.agentconfigfeature.data;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
class AgentConfigFeatureDataAccessConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  SelectAgentConfigFeatureQuery selectAgentConfigFeatureQuery() {
    return new SelectAgentConfigFeatureQuery(jdbcTemplate);
  }
}
