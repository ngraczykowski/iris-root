package com.silenteight.adjudication.engine.features.matchfeaturevalue.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
class DeleteAgentExchangeMatchFeatureValueBatchQuery {

  @Language("PostgreSQL")
  private static final String SQL = "DELETE\n"
      + "FROM ae_agent_exchange_match_feature amfv\n"
      + "WHERE amfv.agent_config_feature_id\n"
      + "    IN (\n"
      + "          SELECT agent_config_feature_id\n"
      + "          FROM ae_agent_config_feature\n"
      + "          WHERE feature\n"
      + "                    IN (:features)\n"
      + "      )\n"
      + "  AND amfv.match_id NOT IN (\n"
      + "    SELECT am.match_id\n"
      + "    FROM ae_pending_recommendation apr\n"
      + "             join ae_match am on apr.alert_id = am.alert_id)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Transactional
  int execute(Iterable<String> features) {
    log.debug("Deleting the following agent exchange feature values: {} ...", features);
    var parameters = new MapSqlParameterSource(Map.of(
        "features", features
    ));
    var deletedCount = jdbcTemplate.update(SQL, parameters);
    log.debug("Deleted agent exchange for match feature values: count={}", deletedCount);
    return deletedCount;
  }
}
