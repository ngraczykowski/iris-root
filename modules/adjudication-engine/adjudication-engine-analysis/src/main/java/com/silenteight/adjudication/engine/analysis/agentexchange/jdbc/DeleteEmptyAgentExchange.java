package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DeleteEmptyAgentExchange {

  private final JdbcTemplate jdbcTemplate;

  void execute() {
    jdbcTemplate.update("DELETE FROM ae_agent_exchange\n"
        + "WHERE agent_exchange_id IN (\n"
        + "    SELECT aae.agent_exchange_id \n"
        + "    FROM ae_agent_exchange aae\n"
        + "    LEFT JOIN ae_agent_exchange_match_feature aaemf "
        + "         ON aae.agent_exchange_id = aaemf.agent_exchange_id\n"
        + "    WHERE aaemf.agent_exchange_match_feature_id IS NULL\n"
        + ")");
  }
}
