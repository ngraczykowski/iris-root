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
          + " WHERE file_name IN (:file_name)\n";
  private static final String FILE_NAME = "file_name";

  private final JdbcTemplate jdbcTemplate;

  public void remove(List<String> fileNames) {
    var query = createQuery();

    for (var fileName : fileNames) {
      var paramMap =
          Map.of(FILE_NAME, fileName);
      query.updateByNamedParam(paramMap);
    }

    query.flush();
  }

  private BatchSqlUpdate createQuery() {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(FILE_NAME, Types.VARCHAR));
    batchSqlUpdate.compile();
    return batchSqlUpdate;
  }
}
