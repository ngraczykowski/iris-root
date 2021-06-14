package com.silenteight.adjudication.engine.app;

import org.springframework.jdbc.core.JdbcTemplate;

class MatchSolutionTestDataAccess {

  static int solvedMatchesCount(JdbcTemplate jdbcTemplate, long analysisId) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*)\n"
            + "FROM ae_match_solution\n"
            + "WHERE analysis_id = ?;", Integer.class, analysisId);
  }
}
