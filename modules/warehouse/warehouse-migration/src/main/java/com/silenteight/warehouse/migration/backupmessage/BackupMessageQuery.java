package com.silenteight.warehouse.migration.backupmessage;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@RequiredArgsConstructor
class BackupMessageQuery {

  private final RowMapper<Message> rowMapper =
      (rs, rowNum) ->
          new Message(
              rs.getLong("id"),
              rs.getBytes("data"),
              rs.getBoolean("migrated")
          );

  private final JdbcTemplate jdbcTemplate;

  @Language("PostgreSQL")
  private static final String COUNT_QUERY =
      "SELECT COUNT(*) FROM (SELECT id FROM warehouse_message_backup) wmb";

  @Language("PostgreSQL")
  private static final String NOT_MIGRATED_RECORD_EXIST_QUERY =
      "SELECT EXISTS(SELECT 1 FROM warehouse_message_backup WHERE migrated ISNULL)";

  @Language("PostgreSQL")
  private static final String MIGRATION_CANDIDATES_QUERY =
      "SELECT * FROM warehouse_message_backup wmb "
          + "WHERE wmb.migrated ISNULL "
          + "ORDER BY wmb.id "
          + "LIMIT ?";

  @Language("PostgreSQL")
  private static final String UPDATE_MIGRATED_FLAG_QUERY =
      "UPDATE warehouse_message_backup wmb SET migrated = ? WHERE wmb.id = ?";

  Boolean notMigratedRecordExist() {
    return jdbcTemplate.queryForObject(NOT_MIGRATED_RECORD_EXIST_QUERY, Boolean.class);
  }

  List<Message> findMigrationCandidates(int batchSize) {
    return jdbcTemplate.query(
        MIGRATION_CANDIDATES_QUERY,
        rowMapper,
        batchSize);
  }

  Long count() {
    return jdbcTemplate.queryForObject(COUNT_QUERY, Long.class);
  }

  void update(Message message) {
    jdbcTemplate.update(
        UPDATE_MIGRATED_FLAG_QUERY,
        ps -> {
          ps.setBoolean(1, message.isMigrated());
          ps.setLong(2, message.getId());
        }
    );
  }
}
