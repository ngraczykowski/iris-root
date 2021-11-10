package com.silenteight.adjudication.engine.dataset.dataset.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class SelectDatasetsByAlertsQuery {

  private static final String SQL = "SELECT DISTINCT dataset_id\n"
      + "FROM ae_dataset_alert\n"
      + "WHERE alert_id IN (:alertIds)";

  private final NamedParameterJdbcTemplate namedJdbcTemplate;

  List<Long> execute(List<Long> alertIds) {
    var parameters = new MapSqlParameterSource(Map.of(
        "alertIds", alertIds
    ));
    var response = namedJdbcTemplate.query(SQL, parameters, ((rs, rowNum) -> rs.getLong(1)));
    return response;
  }
}
