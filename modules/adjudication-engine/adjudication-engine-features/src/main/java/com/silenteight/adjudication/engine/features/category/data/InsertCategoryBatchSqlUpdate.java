package com.silenteight.adjudication.engine.features.category.data;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Collection;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
class InsertCategoryBatchSqlUpdate {

  private final JdbcTemplate jdbcTemplate;

  int execute(Collection<String> categoryNames) {
    var sql = createQuery();
    categoryNames.forEach(sql::update);

    var rowsAffected = sql.flush();

    return IntStream.of(rowsAffected).sum();
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO ae_category(category, created_at)\n"
        + "VALUES (?, now())\n"
        + "ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("category", Types.VARCHAR));

    sql.compile();

    return sql;
  }
}
