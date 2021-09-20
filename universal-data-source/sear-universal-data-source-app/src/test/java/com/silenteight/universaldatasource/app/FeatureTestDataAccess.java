package com.silenteight.universaldatasource.app;

import org.springframework.jdbc.core.JdbcTemplate;

public class FeatureTestDataAccess {

  static int streamedFeaturesCount(JdbcTemplate jdbcTemplate, String matches, String features) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*)\n"
            + "FROM uds_feature_input\n"
            + "WHERE match_name = ? AND feature = ?;", Integer.class, matches, features);
  }
}
