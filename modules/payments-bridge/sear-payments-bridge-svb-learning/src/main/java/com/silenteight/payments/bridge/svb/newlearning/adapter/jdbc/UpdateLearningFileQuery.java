package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
class UpdateLearningFileQuery {

  @Language("PostgreSQL")
  private static final String UPDATE_LEARNING_FILE_STATUS =
      "UPDATE pb_learning_file SET status = :status\n"
          + " WHERE learning_file_id = :fileId";


  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  void update(Long fileId, CsvProcessingFileStatus status) {
    log.info("Update learning file:{} status -> {}", fileId, status);
    int rows = namedParameterJdbcTemplate.update(
        UPDATE_LEARNING_FILE_STATUS,
        Map.of("fileId", fileId, "status", status.toString()));
    log.debug("Updated rows:{}", rows);
  }

}
