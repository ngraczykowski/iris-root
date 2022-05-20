package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;


import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.MatchSolution;

import com.google.protobuf.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
class SelectMatchSolutionQuery {

  private static final MatchSolutionRowMapper
      ROW_MAPPER = new MatchSolutionRowMapper();
  private final JdbcTemplate jdbcTemplate;

  MatchSolution execute(long matchSolutionId) {
    return jdbcTemplate.queryForObject(
        "SELECT ams.analysis_id, ams.match_solution_id, am.alert_id, "
            + "   am.match_id, ams.match_solution_id, ams.created_at\n"
            + "FROM ae_match_solution ams\n"
            + "         JOIN ae_match am ON am.match_id = ams.match_id\n"
            + "WHERE ams.match_solution_id = ?",
        ROW_MAPPER, matchSolutionId);
  }

  private static final class MatchSolutionRowMapper implements RowMapper<MatchSolution> {

    @Override
    public MatchSolution mapRow(ResultSet rs, int rowNum) throws SQLException {
      return MatchSolution
          .newBuilder()
          .setName("analysis/" + rs.getLong(1) + "/"
              + "match-solutions/" + rs.getLong(2))
          .setMatch("alerts/" + rs.getLong(3) + "/"
              + "matches/" + rs.getLong(4))
          .setSolution(rs.getString(5))
          .setCreateTime(Timestamp.newBuilder().setNanos(rs.getTimestamp(6).getNanos()).build())
          .build();
    }
  }
}
