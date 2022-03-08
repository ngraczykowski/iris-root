package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class UpdateFileDataRetention {

  @Language("PostgreSQL")
  private static final String QUERY =
      "UPDATE pb_file_data_retention SET  file_data_removed_at = now()\n"
          + " WHERE pb_file_data_retention.file_name in (:file_names)";
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  void update(List<String> fileNames) {
    namedParameterJdbcTemplate.update(QUERY, Map.of("file_names", fileNames));
  }
}
