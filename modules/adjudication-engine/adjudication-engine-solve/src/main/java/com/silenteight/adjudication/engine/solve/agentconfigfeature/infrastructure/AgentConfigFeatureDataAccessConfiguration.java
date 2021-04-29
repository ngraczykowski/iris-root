package com.silenteight.adjudication.engine.solve.agentconfigfeature.infrastructure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
class AgentConfigFeatureDataAccessConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  InsertAgentConfigFeatureQuery insertAgentConfigFeatureSqlUpdate() {
    var sqlUpdate = new InsertAgentConfigFeatureQuery();

    sqlUpdate.setJdbcTemplate(jdbcTemplate);
    sqlUpdate.afterPropertiesSet();

    return sqlUpdate;
  }

  @Bean
  SelectAgentConfigFeatureQuery selectAgentConfigFeatureQuery() {
    return new SelectAgentConfigFeatureQuery(jdbcTemplate);
  }
}
