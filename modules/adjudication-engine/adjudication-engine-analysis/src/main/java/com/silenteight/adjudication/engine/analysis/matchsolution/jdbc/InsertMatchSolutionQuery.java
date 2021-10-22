package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SaveMatchSolutionRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;

class InsertMatchSolutionQuery {

  private final BatchSqlUpdate sql;

  InsertMatchSolutionQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(
        "INSERT INTO ae_match_solution ("
            + "analysis_id, match_id, created_at, solution, reason, match_context)\n"
            + "VALUES (?, ?, now(), ?, ?, ?)\n"
            + "ON CONFLICT DO NOTHING;");
    sql.declareParameter(new SqlParameter("analysisId", Types.BIGINT));
    sql.declareParameter(new SqlParameter("matchId", Types.BIGINT));
    sql.declareParameter(new SqlParameter("solution", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("reason", Types.OTHER));
    sql.declareParameter(new SqlParameter("match_context", Types.OTHER));

    sql.compile();
  }

  void execute(Collection<SaveMatchSolutionRequest> requests) {
    requests.forEach(this::update);
    sql.flush();
  }

  @SuppressWarnings("FeatureEnvy")
  private void update(SaveMatchSolutionRequest request) {
    sql.update(
        request.getAnalysisId(), request.getMatchId(), request.getSolution(),
        request.getReasonString(), request.getContextString());
  }
}
