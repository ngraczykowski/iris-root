package com.silenteight.payments.bridge.data.retention.service;

import com.silenteight.payments.bridge.data.retention.SendAlertExpiredPortMock;
import com.silenteight.payments.bridge.data.retention.TestApplicationConfiguration;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.data.retention.service.DataRetentionConstants.SEND_ALERTS_EXPIRED_STEP_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.data.retention" },
    excludeFilters =
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = PiiRetentionJobConfiguration.class))
class AlertRetentionJobTest extends BaseBatchTest {

  @Autowired
  private SendAlertExpiredPortMock sendAlertsExpiredPort;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  @Sql(scripts = "ShouldSendExpiredAlerts.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void shouldSendAlertExpiredAndSaveAlertsStatus() {
    var alertsExpiredStep = createStepExecution(SEND_ALERTS_EXPIRED_STEP_NAME).get();
    assertThat(alertsExpiredStep.getReadCount()).isEqualTo(10);
    assertThat(sendAlertsExpiredPort.getAlertsExpiredCount()).isEqualTo(10);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_alert_data_retention WHERE alert_data_removed_at IS NOT NULL ",
        Integer.class)).isEqualTo(10);
  }

  @Nonnull
  private Optional<StepExecution> createStepExecution(String stepName) {
    var jobExecution = jobLauncherTestUtils.launchStep(stepName);
    Assertions.assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    var stepExecution = jobExecution
        .getStepExecutions()
        .stream()
        .filter(
            step -> stepName.equals(step.getStepName()))
        .findFirst();
    assertThat(stepExecution.isPresent()).isTrue();
    return stepExecution;
  }

}
