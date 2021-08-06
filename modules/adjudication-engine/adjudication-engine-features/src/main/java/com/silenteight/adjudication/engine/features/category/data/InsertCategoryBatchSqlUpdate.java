package com.silenteight.adjudication.engine.features.category.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;
import java.util.stream.IntStream;

class InsertCategoryBatchSqlUpdate {

  private final BatchSqlUpdate sql;

  InsertCategoryBatchSqlUpdate(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO ae_category(category, created_at)\n"
        + "VALUES (?, now())\n"
        + "ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("category", Types.VARCHAR));

    sql.compile();
  }

  int execute(Collection<String> categoryNames) {
    categoryNames.forEach(sql::update);

    var rowsAffected = sql.flush();

    return IntStream.of(rowsAffected).sum();
  }
}
