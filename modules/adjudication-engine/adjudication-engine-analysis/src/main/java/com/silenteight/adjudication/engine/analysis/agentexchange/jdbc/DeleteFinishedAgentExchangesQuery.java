package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
class DeleteFinishedAgentExchangesQuery {

  @Language("PostgreSQL")
  public static final String DELETE_MATCH_FEATURES =
      "DELETE\n"
          + "FROM ae_agent_exchange_match_feature\n"
          + "WHERE array [agent_exchange_id, match_id] IN (\n"
          + "    SELECT array [aae.agent_exchange_id, aaemf.match_id]\n"
          + "    FROM ae_agent_exchange aae\n"
          + "             JOIN ae_agent_exchange_match_feature aaemf\n"
          + "                  on aae.agent_exchange_id = aaemf.agent_exchange_id\n"
          + "             JOIN (SELECT amfv.match_id,\n"
          + "                          amfv.agent_config_feature_id\n"
          + "                   FROM ae_match_feature_value amfv\n"
          + "                   LIMIT 16384) existing\n"
          + "                  on aaemf.match_id = existing.match_id\n"
          + "                      AND aaemf.agent_config_feature_id"
          + " = existing.agent_config_feature_id\n"
          + ")\n";

  @Language("PostgreSQL")
  public static final String DELETE_AGENT_EXCHANGES =
      "DELETE\n"
          + "FROM ae_agent_exchange\n"
          + "WHERE agent_exchange_id IN (\n"
          + "    SELECT aae.agent_exchange_id\n"
          + "    FROM ae_agent_exchange aae\n"
          + "             LEFT JOIN ae_agent_exchange_match_feature aaemf\n"
          + "                       on aae.agent_exchange_id = aaemf.agent_exchange_id\n"
          + "    WHERE aaemf.match_id IS NULL)\n";

  private final JdbcTemplate jdbcTemplate;

  @Transactional
  int execute() {
    jdbcTemplate.execute(DELETE_MATCH_FEATURES);
    return jdbcTemplate.update(DELETE_AGENT_EXCHANGES);
  }
}
