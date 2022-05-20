package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
class DeleteReceivedAgentExchangeMatchFeaturesQuery {

  private static final int DEFAULT_LIMIT = 128 * 1024;

  @Language("PostgreSQL")
  public static final String SQL =
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
          + "                   LIMIT ?) existing\n"
          + "                  on aaemf.match_id = existing.match_id\n"
          + "                      AND aaemf.agent_config_feature_id"
          + " = existing.agent_config_feature_id\n"
          + ")\n";

  private final JdbcTemplate jdbcTemplate;

  @Setter
  private int limit = DEFAULT_LIMIT;

  @Transactional
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  int execute() {
    return jdbcTemplate.update(SQL, limit);
  }
}
