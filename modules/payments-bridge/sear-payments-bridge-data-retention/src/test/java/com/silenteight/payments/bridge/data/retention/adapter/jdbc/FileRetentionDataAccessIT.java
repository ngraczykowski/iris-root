package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import com.silenteight.payments.bridge.data.retention.model.FileDataRetention;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Sql
@Import({
    FileRetentionDataAccess.class,
    FindFileDataRetention.class,
    InsertFileDataRetention.class,
    UpdateFileDataRetention.class })
class FileRetentionDataAccessIT extends BaseJdbcTest {

  @Autowired
  private FileRetentionDataAccess fileRetentionDataAccess;

  @Test
  public void shouldFoundOneFile() {
    var before = OffsetDateTime.now().minusDays(1);
    var result = fileRetentionDataAccess.findFileNameBefore(before);
    assertEquals(1, result.size());
    assertEquals("fileName", result.get(0));
  }

  @Test
  public void shouldFoundZeroFiles() {
    var before = OffsetDateTime.now().minusDays(10);
    var result = fileRetentionDataAccess.findFileNameBefore(before);
    assertEquals(0, result.size());
  }

  @Test
  public void shouldInsertFileRetention() {
    fileRetentionDataAccess.create(
        List.of(FileDataRetention.builder().fileName("newfile").build()));
    assertEquals(1, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_file_data_retention WHERE file_name = 'newfile'", Integer.class));
  }

  @Test
  public void shouldUpdateRemoveAt() {
    fileRetentionDataAccess.update(List.of("updateFile"));
    var removedAt = jdbcTemplate.queryForObject(
        "SELECT file_data_removed_at FROM pb_file_data_retention WHERE file_name = 'updateFile'",
        OffsetDateTime.class);
    assertNotNull(removedAt);
  }
}
