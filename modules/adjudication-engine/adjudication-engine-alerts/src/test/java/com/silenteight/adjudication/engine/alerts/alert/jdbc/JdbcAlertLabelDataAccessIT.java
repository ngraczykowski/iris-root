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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Sql
@ContextConfiguration(classes = {
    JdbcAlertLabelDataAccess.class,
    InsertAlertLabelsQuery.class,
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
        "SELECT count(*) FROM ae_alert_labels",
        Integer.class)).isEqualTo(1);
  }

  @Test
  void shouldThrowException() {
    var request = createLabelInsertRequest();
    assertThrows(Exception.class, () -> dataAccess.insertLabels(List.of(request, request)));
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
        "SELECT count(*) FROM ae_alert_labels",
        Integer.class)).isEqualTo(0);
  }
}
