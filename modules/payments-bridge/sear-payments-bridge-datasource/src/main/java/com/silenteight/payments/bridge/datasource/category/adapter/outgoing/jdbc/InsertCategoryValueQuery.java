package com.silenteight.payments.bridge.datasource.category.adapter.outgoing.jdbc;

import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryValue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;
import java.util.stream.IntStream;

public class InsertCategoryValueQuery {

  private final BatchSqlUpdate sql;

  InsertCategoryValueQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO pb_category_value(match_name, category_name, value)\n"
        + "VALUES (?, ?, ?)\n"
        + "ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("match_name", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("category_name", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("value", Types.VARCHAR));

    sql.compile();
  }

  void execute(MatchCategoryValue matchCategoryValue) {
    update(matchCategoryValue);
    sql.flush();
  }

  int execute(Collection<MatchCategoryValue> matchCategoryValueCollection) {
    matchCategoryValueCollection.forEach(this::update);

    var rowsAffected = sql.flush();
    return IntStream.of(rowsAffected).sum();
  }

  private void update(MatchCategoryValue matchCategoryValue) {
    sql.update(
        matchCategoryValue.getMatch(), matchCategoryValue.getCategory(),
        matchCategoryValue.getValue());
  }
}
