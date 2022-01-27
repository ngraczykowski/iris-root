package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import com.silenteight.payments.bridge.svb.newlearning.step.etl.LearningProcessedAlert;
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
    RemoveDuplicatedAlertsQuery.class,
    RemoveDuplicatedHitsQuery.class,
    RemoveDuplicatedActionsQuery.class,
    SelectProcessedAlertsStatusQuery.class,
    InsertAlertResultQuery.class
})
class JdbcLearningDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcLearningDataAccess dataAccess;

  @Test
  void shouldRemoveDuplicatedValues() {
    dataAccess.removeDuplicates();
    assertRemovedAlerts();
    assertRemovedHits();
    assertRemovedActions();
  }

  private void assertRemovedAlerts() {
    var alertsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_alert WHERE fkco_id = 1", Integer.class);
    assertThat(alertsCount).isEqualTo(1);
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
  void shouldSelectAlertProcessingResult() {
    var result = dataAccess.select(1, "fileName");
    assertThat(result.getFailedAlerts()).isEqualTo(1);
    assertThat(result.getSuccessfulAlerts()).isEqualTo(2);
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
}
