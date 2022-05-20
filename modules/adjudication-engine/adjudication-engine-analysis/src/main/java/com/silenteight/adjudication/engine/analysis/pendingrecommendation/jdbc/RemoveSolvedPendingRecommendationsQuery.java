package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveSolvedPendingRecommendationsQuery {

  private final JdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  int execute() {
    // TODO(wkeska): Replace CTE with subselect (AEP-217)
    return jdbcTemplate.update(
        "WITH solved AS (\n"
            + "    SELECT apr.analysis_id || '/' || apr.alert_id AS analysis_alert\n"
            + "    FROM ae_pending_recommendation apr\n"
            + "             JOIN ae_recommendation ar ON apr.alert_id = ar.alert_id\n"
            + "        AND apr.analysis_id = ar.analysis_id\n"
            + ")\n"
            + "DELETE\n"
            + "FROM ae_pending_recommendation apr\n"
            + "WHERE apr.analysis_id || '/' || apr.alert_id \n"
            + "          IN (SELECT s.analysis_alert FROM solved s)");
  }
}
