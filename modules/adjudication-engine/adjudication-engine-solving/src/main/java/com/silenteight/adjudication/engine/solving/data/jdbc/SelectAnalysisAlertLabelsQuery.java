/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
class SelectAnalysisAlertLabelsQuery {
  private final NamedParameterJdbcTemplate jdbcTemplate;
  @Language("PostgreSQL")
  private static final String QUERY = """
          SELECT aal.name, aal.value
          FROM ae_alert_labels aal
          where alert_id = :alert
          """;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public Map<String, String> finalAlertLabels(Long alert) {

    var parameters = new MapSqlParameterSource(Map.of(
        "alert", alert
    ));
    var mapper = new AlertLabelsRowMapper();
    jdbcTemplate.query(QUERY, parameters, mapper);
    return mapper.getLabels();

  }

  @Getter
  private static class AlertLabelsRowMapper
      implements RowMapper<String> {

    private final Map<String, String> labels = new HashMap<>();

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      var category = rs.getString("name");
      var categoryValue = rs.getString("value");
      labels.put(category,categoryValue);
      return category;
    }
  }
}
