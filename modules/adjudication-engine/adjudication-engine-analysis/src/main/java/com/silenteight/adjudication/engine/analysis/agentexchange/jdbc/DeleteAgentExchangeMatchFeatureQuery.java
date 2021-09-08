package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DeleteAgentExchangeMatchFeatureQuery {

  @Language("PostgreSQL")
  private static final String SQL = "DELETE\n"
      + "FROM ae_agent_exchange_match_feature\n"
      + "WHERE array [agent_exchange_id, match_id] IN (\n"
      + "    SELECT array [aae.agent_exchange_id, aaemf.match_id]\n"
      + "    FROM ae_agent_exchange aae\n"
      + "             JOIN ae_agent_exchange_match_feature aaemf\n"
      + "                  on aae.agent_exchange_id = aaemf.agent_exchange_id\n"
      + "             JOIN (SELECT amfv.match_id,\n"
      + "                          amfv.agent_config_feature_id\n"
      + "                   FROM ae_match_feature_value amfv) existing\n"
      + "                  on aaemf.match_id = existing.match_id\n"
      + "                      AND aaemf.agent_config_feature_id = existing.agent_config_feature_id"
      + ")";

  private final JdbcTemplate jdbcTemplate;

  void execute() {
    jdbcTemplate.update(SQL);
  }
}
