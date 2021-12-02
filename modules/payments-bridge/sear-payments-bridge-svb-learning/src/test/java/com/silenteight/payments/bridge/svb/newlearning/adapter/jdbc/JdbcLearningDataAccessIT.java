package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@Import({
    JdbcLearningDataAccess.class,
    RemoveDuplicatedAlertsQuery.class,
    RemoveDuplicatedHitsQuery.class,
    RemoveDuplicatedActionsQuery.class,
    RemoveDuplicatedListedRecordsQuery.class,
    RemoveDuplicatedActionStatusQuery.class,
    RemoveDuplicatedAlertedMessagesQuery.class,
    SelectLearningFilesQuery.class,
    UpdateLearningFileQuery.class })
class JdbcLearningDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcLearningDataAccess dataAccess;

  @Test
  void shouldRemoveDuplicatedValues() {
    dataAccess.removeDuplicates();
    assertRemovedAlerts();
    assertRemovedHits();
    assertRemovedActions();
    assertRemovedListedRecords();
    assertRemovedActionStatus();
    assertRemovedAlertedMessages();
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

  private void assertRemovedListedRecords() {
    var alertsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_listed_record WHERE fkco_id = 1", Integer.class);
    assertThat(alertsCount).isEqualTo(1);
  }

  private void assertRemovedActionStatus() {
    var alertsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_action_status WHERE fkco_id = 1", Integer.class);
    assertThat(alertsCount).isEqualTo(1);
  }

  private void assertRemovedAlertedMessages() {
    var alertsCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_alerted_message WHERE fkco_messages = 1", Integer.class);
    assertThat(alertsCount).isEqualTo(1);
  }
}
