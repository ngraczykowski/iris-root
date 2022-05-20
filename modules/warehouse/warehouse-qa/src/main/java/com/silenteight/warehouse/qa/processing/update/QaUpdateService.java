package com.silenteight.warehouse.qa.processing.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.qa.processing.mapping.AlertDefinition;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class QaUpdateService {

  private static final String PAYLOAD_PARAMETER = "newPayload";
  private static final String ALERT_NAME_PARAMETER = "name";
  private static final String UPDATE_ALERT_SQL = ""
      + "UPDATE warehouse_alert "
      + "SET payload = payload || TO_JSONB(:newPayload::jsonb) "
      + "WHERE name = :name";

  @NonNull
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public void update(AlertDefinition alertDefinition) {
    log.trace("Persisting qa data, alertName={}", alertDefinition.getName());
    MapSqlParameterSource parameters = toSqlParameters(alertDefinition);
    jdbcTemplate.update(UPDATE_ALERT_SQL, parameters);
  }

  private static MapSqlParameterSource toSqlParameters(AlertDefinition alertDefinition) {
    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue(ALERT_NAME_PARAMETER, alertDefinition.getName());
    parameters.addValue(PAYLOAD_PARAMETER, alertDefinition.getPayload());
    return parameters;
  }
}
