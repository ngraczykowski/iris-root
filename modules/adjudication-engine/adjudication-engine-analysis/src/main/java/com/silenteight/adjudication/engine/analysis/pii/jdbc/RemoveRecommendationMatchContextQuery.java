package com.silenteight.adjudication.engine.analysis.pii.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class RemoveRecommendationMatchContextQuery {

  private static final String SQL =
      "UPDATE ae_recommendation\n"
          + "SET match_contexts = '[]'\n"
          + "WHERE alert_id IN (:alertIds)";

  private final NamedParameterJdbcTemplate namedJdbcTemplate;

  void execute(List<Long> alertIds) {
    var parameters = new MapSqlParameterSource(Map.of(
        "alertIds", alertIds
    ));

    namedJdbcTemplate.update(SQL, parameters);
  }
}
