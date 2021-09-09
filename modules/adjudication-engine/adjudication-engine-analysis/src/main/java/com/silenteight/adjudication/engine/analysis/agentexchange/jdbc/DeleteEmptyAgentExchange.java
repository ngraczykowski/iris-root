package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DeleteEmptyAgentExchange {

  @Language("PostgreSQL")
  private static final String SQL = "DELETE FROM ae_agent_exchange\n"
      + "WHERE agent_exchange_id "
      + "NOT IN (SELECT agent_exchange_id FROM ae_agent_exchange_match_feature)";

  private final JdbcTemplate jdbcTemplate;

  void execute() {
    jdbcTemplate.update(SQL);
  }
}
