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
    RemoveFileCsvRowsQuery.class,
    RemoveLearningAlertsQuery.class,
    RemoveHitsQuery.class,
    RemoveActionsQuery.class })
class RemoveFileDataIT extends BaseJdbcTest {

  @Autowired
  private JdbcLearningDataAccess jdbcLearningDataAccess;

  @Test
  void shouldRemoveCsvRows() {
    jdbcLearningDataAccess.removeCsvRows(List.of(1L, 2L, 3L));
    var fileRowsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_csv_row WHERE file_name = 'learning/mocked_learning.csv'",
        Integer.class);
    assertThat(fileRowsCount).isEqualTo(0);
  }

  @Test
  void shouldRemoveAlerts() {
    jdbcLearningDataAccess.removeAlerts(List.of(1L, 2L, 3L));
    var fileRowsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_alert WHERE file_name = 'learning/mocked_learning.csv'",
        Integer.class);
    assertThat(fileRowsCount).isEqualTo(0);
  }

  @Test
  void shouldRemoveHits() {
    jdbcLearningDataAccess.removeHits(List.of(1L, 2L));
    var fileRowsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_hit WHERE learning_hit_id in (1, 2)",
        Integer.class);
    assertThat(fileRowsCount).isEqualTo(0);
  }

  @Test
  void shouldRemoveActions() {
    jdbcLearningDataAccess.removeActions(List.of(3L));
    var fileRowsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_action WHERE learning_action_id in (3)",
        Integer.class);
    assertThat(fileRowsCount).isEqualTo(0);
  }
}
