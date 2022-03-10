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
class DeleteMatchFeatureValueBatchQuery {

  @Language("PostgreSQL")
  private static final String SQL = "DELETE\n"
      + "FROM ae_match_feature_value\n"
      + "WHERE agent_config_feature_id\n"
      + "IN (\n"
      + "    SELECT agent_config_feature_id\n"
      + "    FROM ae_agent_config_feature\n"
      + "    WHERE feature\n"
      + "    IN (:features)\n"
      + ")";
  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Transactional
  int execute(Iterable<String> features) {
    log.debug("Deleting the following match feature values: {} ...", features);
    var parameters = new MapSqlParameterSource(Map.of(
        "features", features
    ));
    var deletedCount = jdbcTemplate.update(SQL, parameters);
    log.debug("Deleted match feature values: count={}", deletedCount);
    return deletedCount;
  }
}
