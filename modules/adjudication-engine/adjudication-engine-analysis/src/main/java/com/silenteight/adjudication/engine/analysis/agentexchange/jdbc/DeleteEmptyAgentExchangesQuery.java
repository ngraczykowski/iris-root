package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
class DeleteEmptyAgentExchangesQuery {

  @Language("PostgreSQL")
  public static final String SQL =
      "DELETE\n"
          + "FROM ae_agent_exchange\n"
          + "WHERE agent_exchange_id IN (\n"
          + "    SELECT aae.agent_exchange_id\n"
          + "    FROM ae_agent_exchange aae\n"
          + "    WHERE NOT EXISTS(\n"
          + "            SELECT 1\n"
          + "            FROM ae_agent_exchange_match_feature aaemf\n"
          + "            WHERE aae.agent_exchange_id = aaemf.agent_exchange_id)\n"
          + ")";

  private final JdbcTemplate jdbcTemplate;

  @Transactional
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  int execute() {
    return jdbcTemplate.update(SQL);
  }
}
