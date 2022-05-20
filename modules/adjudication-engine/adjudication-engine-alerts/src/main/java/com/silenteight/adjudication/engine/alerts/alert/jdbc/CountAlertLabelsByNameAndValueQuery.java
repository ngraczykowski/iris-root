package com.silenteight.adjudication.engine.alerts.alert.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
class CountAlertLabelsByNameAndValueQuery {

  @Language("PostgreSQL")
  private static final String SQL = "SELECT count(alert_id) FROM ae_alert_labels "
      + "WHERE name=:name AND value=:value";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  long execute(String name, String value) {
    var parameters = new MapSqlParameterSource(Map.of(
        "name", name,
        "value", value
    ));

    var count = jdbcTemplate.queryForObject(SQL, parameters, Long.class);
    log.debug("Count alert labels by name:{} value:{} is:{}", name, value, count);
    return count;
  }
}
