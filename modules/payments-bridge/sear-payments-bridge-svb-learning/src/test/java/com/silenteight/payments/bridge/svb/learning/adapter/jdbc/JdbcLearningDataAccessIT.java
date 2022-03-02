package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import com.silenteight.payments.bridge.svb.learning.step.etl.LearningProcessedAlert;
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
class JdbcLearningDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcLearningDataAccess dataAccess;

  @Test
  void shouldRemoveDuplicatedValues() {
    dataAccess.removeDuplicates();
    assertRemovedHits();
    assertRemovedActions();
  }

  private void assertRemovedHits() {
    var alertsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_hit WHERE fkco_messages = 1", Integer.class);
    assertThat(alertsCount).isEqualTo(1);
  }

  private void assertRemovedActions() {
    var alertsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_action WHERE fkco_messages = 1", Integer.class);
    assertThat(alertsCount).isEqualTo(1);
  }

  @Test
  void shouldInsertAlertResult() {
    dataAccess.saveResult(List.of(LearningProcessedAlert
        .builder()
        .result("SUCCESS")
        .jobId(1L)
        .fileName("someFile")
        .fkcoVSystemId("fkcoSystem")
        .build()));

    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_processed_alert WHERE file_name = 'someFile'",
        Integer.class)).isEqualTo(1);
  }

  @Test
  void shouldCheckThatFileIsStored() {
    var isStored = dataAccess.isFileStored("someFile");
    assertThat(isStored).isEqualTo(true);
  }

  @Test
  void shouldCheckThatFileIsNotStored() {
    var isStored = dataAccess.isFileStored("fileName");
    assertThat(isStored).isEqualTo(false);
  }

  @Test
  void shouldSelectProcessResult() {
    var result = dataAccess.select(1L, "fileName");
    assertThat(result.getSuccessfulAlerts()).isEqualTo(2);
    assertThat(result.getFailedAlerts()).isEqualTo(2);
  }

  @Test
  void shouldSelectProcessResultWhenOnlyFailed() {
    var result = dataAccess.select(2L, "fileName");
    assertThat(result.getSuccessfulAlerts()).isEqualTo(0);
    assertThat(result.getFailedAlerts()).isEqualTo(1);
  }

  @Test
  void shouldSelectProcessResultWhenOnlySuccessful() {
    var result = dataAccess.select(3L, "fileName");
    assertThat(result.getSuccessfulAlerts()).isEqualTo(1);
    assertThat(result.getFailedAlerts()).isEqualTo(0);
  }
}
