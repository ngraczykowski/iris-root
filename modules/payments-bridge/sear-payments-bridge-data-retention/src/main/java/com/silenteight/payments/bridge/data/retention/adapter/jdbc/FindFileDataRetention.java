package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
class FindFileDataRetention {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT file_name FROM pb_file_data_retention\n"
          + "WHERE created_at < ? AND\n"
          + " file_data_removed_at is null";

  private final JdbcTemplate jdbcTemplate;

  List<String> findAlertTimeBefore(OffsetDateTime dateTime) {
    return jdbcTemplate.query(QUERY,
        (rs, rowNum) -> rs.getString(1), dateTime);
  }
}
