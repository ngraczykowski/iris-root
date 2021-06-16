package com.silenteight.adjudication.engine.app;

import org.springframework.jdbc.core.JdbcTemplate;

class RecommendationTestDataAccess {

  static int generatedRecommendationCount(JdbcTemplate jdbcTemplate, long analysisId) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*)\n"
            + "FROM ae_recommendation\n"
            + "WHERE analysis_id = ?;", Integer.class, analysisId);
  }
}
