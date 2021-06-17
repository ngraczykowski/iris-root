package com.silenteight.adjudication.engine.app;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

class MatchSolutionTestDataAccess {

  static int solvedMatchesCount(JdbcTemplate jdbcTemplate, long analysisId) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*)\n"
            + "FROM ae_match_solution\n"
            + "WHERE analysis_id = ?;", Integer.class, analysisId);
  }

  static Map<String, Object> matchSolutions(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForMap("SELECT *\n"
        + "FROM ae_alert_match_solutions_query");
  }

  static Map<String, Object> alertMatches(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForMap("SELECT *\n"
        + "FROM ae_alert_matches_query");
  }
}
