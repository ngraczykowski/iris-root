package com.silenteight.universaldatasource.app.category;

import org.springframework.jdbc.core.JdbcTemplate;

class CategoryTestDataAccess {

  static int streamedCategoriesCount(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*)\n"
            + "FROM uds_category;", Integer.class);
  }

  static int streamedCategoryValueCount(JdbcTemplate jdbcTemplate) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*)\n"
            + "FROM uds_category_value;", Integer.class);
  }
}
