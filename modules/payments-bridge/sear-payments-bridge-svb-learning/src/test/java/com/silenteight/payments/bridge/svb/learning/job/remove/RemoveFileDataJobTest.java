package com.silenteight.payments.bridge.svb.learning.job.remove;

import com.silenteight.payments.bridge.svb.learning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_ACTION_STEP_NAME;
import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_ALERT_STEP_NAME;
import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_CSV_ROW_STEP_NAME;
import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_HIT_STEP_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.learning.job.remove",
    "com.silenteight.payments.bridge.svb.learning.step",
    "com.silenteight.payments.bridge.svb.learning.adapter",
    "com.silenteight.payments.bridge.svb.learning.config",
    "com.silenteight.payments.bridge.svb.learning.service",
    "com.silenteight.payments.bridge.svb.learning.mapping",
    "com.silenteight.payments.bridge.common.resource.csv.file.provider.service.def" })
class RemoveFileDataJobTest extends BaseBatchTest {

  @Test
  @Sql(scripts = "RemoveFileDataJobTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testRemovingCsvRowsStep() {
    var transformAlertStep = createStepExecution(REMOVE_CSV_ROW_STEP_NAME).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(3);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_csv_row",
        Integer.class)).isEqualTo(0);
  }

  @Test
  @Sql(scripts = "RemoveFileDataJobTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testRemovingAlertsStep() {
    var transformAlertStep = createStepExecution(REMOVE_ALERT_STEP_NAME).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(3);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_alert",
        Integer.class)).isEqualTo(0);
  }

  @Test
  @Sql(scripts = "RemoveFileDataJobTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testRemovingHitsStep() {
    var transformAlertStep = createStepExecution(REMOVE_HIT_STEP_NAME).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(2);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_hit",
        Integer.class)).isEqualTo(0);
  }

  @Test
  @Sql(scripts = "RemoveFileDataJobTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testRemovingActionsStep() {
    var transformAlertStep = createStepExecution(REMOVE_ACTION_STEP_NAME).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(1);
    assertThat(jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_learning_action",
        Integer.class)).isEqualTo(0);
  }


  @Nonnull
  private Optional<StepExecution> createStepExecution(String stepName) {
    var jobParameters = new JobParametersBuilder()
        .addString("file-name", "learning/mocked_learning.csv")
        .toJobParameters();
    var jobExecution = jobLauncherTestUtils.launchStep(stepName, jobParameters);
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
