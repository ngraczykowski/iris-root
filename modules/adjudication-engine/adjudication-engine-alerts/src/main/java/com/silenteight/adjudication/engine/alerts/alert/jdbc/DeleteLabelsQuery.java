package com.silenteight.adjudication.engine.alerts.alert.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class DeleteLabelsQuery {

  @Language("PostgreSQL")
  private static final String SQL = "DELETE\n"
      + "FROM ae_alert_labels aal\n"
      + "WHERE aal.alert_id IN (:alertIds)\n"
      + "  AND aal.name IN (:names)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Transactional
  void execute(List<Long> alertIds, List<String> labelNames) {
    var parameters = new MapSqlParameterSource(Map.of(
        "alertIds", alertIds,
        "names", labelNames
    ));

    jdbcTemplate.update(SQL, parameters);
  }
}
