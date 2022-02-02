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
    SelectProcessedAlertsStatusQuery.class,
    InsertAlertResultQuery.class
})
class JdbcLearningDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcLearningDataAccess dataAccess;

  @Test
  void shouldSelectAlertProcessingResult() {
    var result = dataAccess.select(1, "fileName");
    assertThat(result.getFailedAlerts()).isEqualTo(2);
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
