package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningFile;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class SelectLearningFilesQuery {

  private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");
  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT learning_file_id,file_name,bucket_name,status,created_at FROM pb_learning_file\n"
          + " WHERE learning_file_id = ?";

  private static final LearningFileRowMapper ROW_MAPPER = new LearningFileRowMapper();
  private final JdbcTemplate jdbcTemplate;

  Optional<LearningFile> execute(Long fileId) {
    return Optional.ofNullable(jdbcTemplate.queryForObject(SQL, ROW_MAPPER, fileId));
  }

  private static final class LearningFileRowMapper implements RowMapper<LearningFile> {

    @Override
    public LearningFile mapRow(ResultSet rs, int rowNum) throws SQLException {
      return LearningFile.builder()
          .learningFileId(rs.getLong(1))
          .fileName(rs.getString(2))
          .bucketName(rs.getString(3))
          .status(CsvProcessingFileStatus.valueOf(rs.getString(4)))
          .createdAt(OffsetDateTime.ofInstant(
              Instant.ofEpochMilli(rs.getTimestamp(5).getTime()), UTC_ZONE_ID))
          .build();
    }
  }
}
