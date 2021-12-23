package com.silenteight.warehouse.production.persistence.insert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
class LabelPersistenceService {

  private static final String ALERT_ID_PARAMETER = "alertId";
  private static final String LABEL_NAME_PARAMETER = "name";
  private static final String LABEL_VALUE_PARAMETER = "value";

  private static final String INSERT_ALERT_LABEL_SQL =
      "INSERT INTO warehouse_alert_label (alert_id, name, value)"
          + " VALUES (:alertId, :name, :value)"
          + " ON CONFLICT (alert_id, name) DO UPDATE SET"
          + " value = excluded.value";

  void persist(
      NamedParameterJdbcTemplate jdbcTemplate,
      long persistedAlertId, Map<String, String> labels) {

    MapSqlParameterSource[] parameters = mapToSqlParameters(persistedAlertId, labels);
    jdbcTemplate.batchUpdate(INSERT_ALERT_LABEL_SQL, parameters);
    log.debug("Persisted {} labels for alertId:{}", labels.size(), persistedAlertId);
  }

  private MapSqlParameterSource[] mapToSqlParameters(
      long persistedAlertId, Map<String, String> labels) {

    return labels
        .entrySet()
        .stream()
        .map(entry -> toSqlParameters(persistedAlertId, entry.getKey(), entry.getValue()))
        .toArray(MapSqlParameterSource[]::new);
  }

  private static MapSqlParameterSource toSqlParameters(
      long persistedAlertId, String labelName, String labelValue) {

    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue(ALERT_ID_PARAMETER, persistedAlertId);
    parameters.addValue(LABEL_NAME_PARAMETER, labelName);
    parameters.addValue(LABEL_VALUE_PARAMETER, labelValue);
    return parameters;
  }
}
