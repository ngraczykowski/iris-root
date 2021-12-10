package com.silenteight.adjudication.engine.alerts.alert.jdbc;

import com.silenteight.adjudication.engine.alerts.alert.domain.RemoveLabelsRequest;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.silenteight.adjudication.engine.alerts.alert.AlertFixtures.createLabelInsertRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@ContextConfiguration(classes = {
    JdbcAlertLabelDataAccess.class,
    InsertAlertLabelsQuery.class,
    CountAlertLabelsByNameAndValueQuery.class,
    CountAlertLabelsSubsetInSet.class,
    DeleteLabelsQuery.class
})
class JdbcAlertLabelDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcAlertLabelDataAccess dataAccess;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldInsertLabels() {
    var request = createLabelInsertRequest();
    dataAccess.insertLabels(List.of(request));
    assertThat(jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM ae_alert_labels WHERE alert_id=1 AND name LIKE 'labeleczka'",
        Integer.class)).isEqualTo(1);
  }

  @Test
  void shouldCountLabels() {
    long count = dataAccess.countByNameAndValue("any", "value");
    assertThat(count).isEqualTo(1);
  }

  @Test
  void shouldHaveSameLearingAndSolvingAlerts() {
    long learning = dataAccess.countByNameAndValue("source", "learning");
    long solving = dataAccess.countByNameAndValue("source", "solving");

    assertThat(learning).isEqualTo(3);
    assertThat(solving).isEqualTo(2);
  }

  @Test
  void shouldHaveOneLearningInSolving() {
    Long learningInSolving = dataAccess.countAlertsLearningInSolvingSet();

    assertThat(learningInSolving).isEqualTo(1);
  }

  @Test
  void shouldHaveOneSolvingInLearning() {
    long solvingInLearning = dataAccess.countAlertsSolvingInLearningSet();
    assertThat(solvingInLearning).isEqualTo(1);
  }

  @Test
  void shouldRemoveLabels() {
    var request = createLabelInsertRequest();
    dataAccess.insertLabels(List.of(request));

    dataAccess.removeLabels(RemoveLabelsRequest
        .builder()
        .alertIds(List.of(1L))
        .labelNames(List.of("labeleczka"))
        .build());

    assertThat(jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM ae_alert_labels WHERE name LIKE 'labeleczka'",
        Integer.class)).isZero();
  }
}
