package com.silenteight.adjudication.engine.dataset.dataset.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
class CreateSingleMatchFilteredDatasetQuery {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Language("PostgreSQL")
  private static final String SQL = "INSERT INTO ae_dataset_alert\n"
      + "SELECT :datasetId, aea.alert_id\n"
      + "FROM ae_alert aea\n"
      + "LEFT JOIN ae_alert_labels aal ON aea.alert_id = aal.alert_id\n"
      + "LEFT JOIN ae_match am ON aea.alert_id = am.alert_id\n"
      + "WHERE aea.alerted_at >= :startDate and aea.alerted_at < :endDate\n"
      + "AND (:labelsValues IS NULL OR (aal.name || aal.value IN (:labelsValues)))\n"
      + "AND am.match_id IS NOT NULL\n"
      + "AND aea.alert_id IN (SELECT alert_id FROM single_match_alerts_temp)\n"
      + "ON CONFLICT DO NOTHING";

  void execute(
      long datasetId, List<String> labelsValues, OffsetDateTime startDate, OffsetDateTime endDate) {

    var jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();

    jdbcTemplate.execute("CREATE TEMPORARY TABLE IF NOT EXISTS single_match_alerts_temp AS\n"
        + "SELECT ae_alert.alert_id, COUNT(*)\n"
        + "FROM ae_alert\n"
        + "JOIN ae_match am on ae_alert.alert_id = am.alert_id\n"
        + "GROUP BY ae_alert.alert_id\n"
        + "HAVING COUNT(*) = 1");

    var parameters = new MapSqlParameterSource("datasetId", datasetId);
    parameters.addValue("labelsValues", labelsValues);
    parameters.addValue("startDate", startDate);
    parameters.addValue("endDate", endDate);
    namedParameterJdbcTemplate.update(SQL, parameters);

    jdbcTemplate.execute("DROP TABLE IF EXISTS single_match_alerts_temp");

  }
}
