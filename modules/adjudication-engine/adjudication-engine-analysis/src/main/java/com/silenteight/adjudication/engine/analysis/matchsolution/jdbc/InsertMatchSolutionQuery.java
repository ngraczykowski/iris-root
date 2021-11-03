package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SaveMatchSolutionRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Collection;

@Component
@RequiredArgsConstructor
class InsertMatchSolutionQuery {

  private final JdbcTemplate jdbcTemplate;

  void execute(Collection<SaveMatchSolutionRequest> requests) {
    var sql = createQuery();
    requests.forEach(r -> update(r, sql));
    sql.flush();
  }

  @SuppressWarnings("FeatureEnvy")
  private void update(SaveMatchSolutionRequest request, BatchSqlUpdate sql) {
    sql.update(
        request.getAnalysisId(), request.getMatchId(), request.getSolution(),
        request.getReasonString(), request.getContextString());
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

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

    return sql;
  }
}
