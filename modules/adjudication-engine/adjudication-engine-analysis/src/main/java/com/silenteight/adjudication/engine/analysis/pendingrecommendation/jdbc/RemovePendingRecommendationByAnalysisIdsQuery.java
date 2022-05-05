package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class RemovePendingRecommendationByAnalysisIdsQuery {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  List<Long> execute(List<Long> analysisIds) {

    var paramMap = Map.of("ids", analysisIds);

    return jdbcTemplate.queryForList(
        "DELETE\n"
            + "FROM ae_pending_recommendation apr\n"
            + "WHERE apr.analysis_id IN (:ids)\n"
            + "RETURNING apr.alert_id",
        paramMap, Long.class);
  }
}
