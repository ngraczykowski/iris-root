package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class DeleteAgentExchangeMatchFeatureQuery {

  @Language("PostgreSQL")
  private static final String SQL = "DELETE\n"
      + "FROM ae_agent_exchange_match_feature\n"
      + "WHERE ARRAY[agent_exchange_id, agent_config_feature_id] IN (\n"
      + "    SELECT ARRAY[aae.agent_exchange_id, aaemf.agent_config_feature_id]\n"
      + "    FROM ae_agent_exchange_match_feature aaemf\n"
      + "             JOIN ae_agent_exchange aae\n"
      + "                  ON aae.agent_exchange_id = aaemf.agent_exchange_id\n"
      + "             JOIN ae_agent_config_feature aacf "
      + "                 ON aacf.agent_config_feature_id = aaemf.agent_config_feature_id\n"
      + "    WHERE aacf.feature IN (:features)\n"
      + "    AND aae.request_id = (:requestID))\n"
      + "  AND match_id = (:matchID)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  void execute(UUID agentExchangeRequestID, long matchID, List<String> features) {
    MapSqlParameterSource parameters = new MapSqlParameterSource("features", features);
    parameters.addValue("requestID", agentExchangeRequestID);
    parameters.addValue("matchID", matchID);

    jdbcTemplate.update(SQL, parameters);
  }
}
