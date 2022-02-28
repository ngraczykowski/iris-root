package com.silenteight.payments.bridge.data.retention.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql
@Import({ FileRetentionDataAccess.class, FindFileDataRetention.class })
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
}
