package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@Import({
    JdbcLearningDataAccess.class,
    SelectProcessedAlertsStatusQuery.class,
    InsertAlertResultQuery.class,
    CheckIsFileStoredQuery.class,
    RemoveDuplicatedHitsQuery.class,
    RemoveDuplicatedActionsQuery.class,
    RemoveFileCsvRowsQuery.class })
class RemoveFileDataIT extends BaseJdbcTest {

  @Autowired
  private JdbcLearningDataAccess jdbcLearningDataAccess;


  @Test
  void shouldRemoveCsvRows() {
    jdbcLearningDataAccess.removeFileData(List.of("learning/mocked_learning.csv"));
    var fileRowsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_csv_row WHERE file_name = 'learning/mocked_learning.csv'",
        Integer.class);
    assertThat(fileRowsCount).isEqualTo(0);
  }
}
