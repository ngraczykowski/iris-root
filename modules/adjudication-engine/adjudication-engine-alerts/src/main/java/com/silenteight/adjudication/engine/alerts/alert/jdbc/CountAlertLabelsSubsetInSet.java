package com.silenteight.adjudication.engine.alerts.alert.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
class CountAlertLabelsSubsetInSet {

  @Language("PostgreSQL")
  private static final String SQL = "SELECT count(alert_id) FROM ae_alert_labels "
      + "WHERE name=:subsetName AND value=:subsetValue "
      + "AND alert_id IN "
      + "(SELECT alert_id FROM ae_alert_labels WHERE name LIKE :name AND value LIKE :value)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  long execute(String subsetName, String subsetValue, String name, String value) {
    var parameters = new MapSqlParameterSource(Map.of(
        "subsetName", subsetName,
        "subsetValue", subsetValue,
        "name", name,
        "value", value
    ));

    var count = jdbcTemplate.queryForObject(SQL, parameters, Long.class);
    log.debug(
        "Count alert Subset marked by subsetName:{} subsetValue:{} in Set name:{} value:{} is:{}",
        subsetName, subsetValue,
        name, value,
        count);
    return count;
  }
}
