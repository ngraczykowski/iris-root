package com.silenteight.payments.bridge.data.retention.service;

import com.silenteight.payments.bridge.data.retention.SendPersonalInformationExpiredMock;
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

import static com.silenteight.payments.bridge.data.retention.service.DataRetentionConstants.SEND_REMOVE_PII_STEP_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.data.retention" },
    excludeFilters =
    @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = AlertRetentionJobConfiguration.class))
class PiiRetentionJobTest extends BaseBatchTest {

  @Autowired
  private SendPersonalInformationExpiredMock sendPersonalInformationExpired;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  @Sql(scripts = "ShouldSendExpiredAlerts.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  public void shouldSendRemovePiiAndSaveAlertsStatus() {
    var alertsExpiredStep = createStepExecution(SEND_REMOVE_PII_STEP_NAME).get();
    assertThat(alertsExpiredStep.getReadCount()).isEqualTo(10);
    assertThat(sendPersonalInformationExpired.getPiiExpiredCount()).isEqualTo(10);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_alert_data_retention "
            + "WHERE pb_alert_data_retention.pii_removed_at IS NOT NULL ",
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
