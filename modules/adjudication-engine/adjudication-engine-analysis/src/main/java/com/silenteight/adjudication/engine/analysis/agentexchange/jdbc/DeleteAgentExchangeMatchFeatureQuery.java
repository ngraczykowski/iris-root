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
      + "WHERE ARRAY [agent_exchange_id::text, match_id::text, "
      + "             agent_exchange_match_feature_id::text] IN (\n"
      + "    SELECT ARRAY [exchangeIds, matchIds, features] AS ab\n"
      + "    FROM unnest(array((SELECT aae.agent_exchange_id::text\n"
      + "                       FROM unnest(ARRAY[:requestIds ]) aid\n"
      + "                                JOIN ae_agent_exchange aae\n"
      + "                                     ON aae.request_id = aid))\n"
      + "             , ARRAY[:matchIds ]::text[]\n"
      + "             , array((SELECT aacf.agent_config_feature_id::text\n"
      + "                      FROM unnest(ARRAY[:features ]) f\n"
      + "                               JOIN ae_agent_config_feature aacf "
      + "                                 ON aacf.feature = f))) x(exchangeIds, matchIds, features)"
      + ")";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  void execute(List<UUID> agentExchangeRequestId, List<Long> matchId, List<String> features) {
    MapSqlParameterSource parameters = new MapSqlParameterSource("features", features);
    parameters.addValue("requestIds", agentExchangeRequestId);
    parameters.addValue("matchIds", matchId);

    jdbcTemplate.update(SQL, parameters);
  }
}
