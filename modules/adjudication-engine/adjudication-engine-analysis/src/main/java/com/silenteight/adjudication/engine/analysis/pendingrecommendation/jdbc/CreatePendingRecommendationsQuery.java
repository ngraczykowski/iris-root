package com.silenteight.adjudication.engine.analysis.pendingrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@RequiredArgsConstructor
@Component
class CreatePendingRecommendationsQuery {

  private final SqlUpdate query;

  @Autowired
  CreatePendingRecommendationsQuery(JdbcTemplate jdbcTemplate) {
    query = new SqlUpdate();

    query.setJdbcTemplate(jdbcTemplate);
    query.setSql("INSERT INTO ae_pending_recommendation\n"
        + "SELECT aaa.analysis_id, aaa.alert_id, now()\n"
        + "FROM ae_analysis_alert aaa\n"
        + "         LEFT JOIN ae_recommendation ar\n"
        + "                   ON aaa.alert_id = ar.alert_id\n"
        + "                          AND aaa.analysis_id = ar.analysis_id\n"
        + "         LEFT JOIN ae_pending_recommendation apr\n"
        + "                   ON aaa.alert_id = apr.alert_id\n"
        + "                          AND aaa.analysis_id = ar.analysis_id\n"
        + "WHERE aaa.analysis_id = ?\n"
        + "  AND ar.analysis_id IS NULL\n"
        + "  AND apr.analysis_id IS NULL\n"
        + "ON CONFLICT DO NOTHING");
    query.setParameters(new SqlParameter("analysis_id", Types.BIGINT));
  }

  int execute(long analysisId) {
    var count = query.update(analysisId);
    return count;
  }
}
