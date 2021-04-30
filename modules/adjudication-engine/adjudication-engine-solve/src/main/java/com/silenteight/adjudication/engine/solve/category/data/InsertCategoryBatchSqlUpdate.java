package com.silenteight.adjudication.engine.solve.category.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;

class InsertCategoryBatchSqlUpdate {

  private final BatchSqlUpdate sql;

  InsertCategoryBatchSqlUpdate(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO ae_category(category, created_at)"
        + " VALUES (?, now())"
        + " ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("category", Types.VARCHAR));

    sql.compile();
  }

  void execute(String categoryName) {
    sql.update(categoryName);
  }

  int[] flush() {
    return sql.flush();
  }
}
