package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

  @Transactional
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  void execute(List<UUID> agentExchangeRequestId, List<Long> matchId, List<String> features) {
    var parameters = new MapSqlParameterSource(Map.of(
        "features", features,
        "requestIds", agentExchangeRequestId,
        "matchIds", matchId
    ));

    jdbcTemplate.update(SQL, parameters);
  }
}
