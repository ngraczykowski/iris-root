package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
class SelectAnalysisByPendingRecommendationMatches {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  List<Long> execute(List<Long> analysisIds) {
    var paramMap = Map.of("ids", analysisIds);

    return jdbcTemplate.queryForList(
        "SELECT distinct aa.analysis_id\n"
            + "from ae_pending_recommendation apr\n"
            + "         JOIN ae_match am on am.alert_id = apr.alert_id\n"
            + "         JOIN ae_analysis aa on aa.analysis_id = apr.analysis_id\n"
            + "WHERE am.match_id IN (:ids)",
        paramMap, Long.class);
  }
}
