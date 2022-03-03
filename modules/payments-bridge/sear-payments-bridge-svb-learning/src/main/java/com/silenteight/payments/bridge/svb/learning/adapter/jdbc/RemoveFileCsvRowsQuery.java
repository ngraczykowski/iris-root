package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class RemoveFileCsvRowsQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "DELETE FROM pb_learning_csv_row\n"
          + " WHERE pb_learning_csv_row.learning_csv_row_id IN (:row_id)\n";
  private static final String ROW_ID = "row_id";

  private final JdbcTemplate jdbcTemplate;

  public void remove(List<Long> rowIds) {
    var query = createQuery();

    for (var rowId : rowIds) {
      var paramMap =
          Map.of(ROW_ID, rowId);
      query.updateByNamedParam(paramMap);
    }

    query.flush();
  }

  private BatchSqlUpdate createQuery() {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(ROW_ID, Types.BIGINT));
    batchSqlUpdate.compile();
    return batchSqlUpdate;
  }
}
