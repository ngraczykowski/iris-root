package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
@RequiredArgsConstructor
class InsertFileDataRetention {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_file_data_retention(file_name, created_at)\n"
          + " VALUES (?, now())\n"
          + " ON CONFLICT (file_name) DO UPDATE SET created_at = excluded.created_at";

  private final JdbcTemplate jdbcTemplate;

  void update(Iterable<FileDataRetention> fileDataRetentions) {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter("file_name", Types.VARCHAR));
    batchSqlUpdate.compile();

    fileDataRetentions.forEach(file -> batchSqlUpdate.update(
        file.getFileName()));
    batchSqlUpdate.flush();
  }
}
