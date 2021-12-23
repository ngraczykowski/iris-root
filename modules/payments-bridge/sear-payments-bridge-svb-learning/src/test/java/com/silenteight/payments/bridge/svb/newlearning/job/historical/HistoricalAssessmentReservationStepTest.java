package com.silenteight.payments.bridge.svb.newlearning.job.historical;

import com.silenteight.payments.bridge.svb.newlearning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_ASSESSMENT_RESERVATION_STEP;
import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_ASSESSMENT_STORE_STEP;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.historical",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter" })
class HistoricalAssessmentReservationStepTest extends BaseBatchTest {


  @Test
  @Sql(scripts = "HistoricalAssessmentReservationStepTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void shouldRunReservationStepTwice() {
    var jobId = runReservationStep();
    var count = fetchReservedForJob(jobId);
    Assertions.assertThat(count).isEqualTo(3);
    // Second time on conflict do nothing should not put any data so result should be the same.
    var secondJobId = runReservationStep();

    var reservedFirstJob = fetchReservedForJob(jobId);
    var reservedSecondJob = fetchReservedForJob(secondJobId);
    // On second reservation execution it should already been assigned to first execution only.
    Assertions.assertThat(reservedFirstJob).isEqualTo(3);
    Assertions.assertThat(reservedSecondJob).isEqualTo(0);
  }

  private long fetchReservedForJob(long jobId) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM pb_learning_historical_reservation WHERE job_id=" + jobId,
        Number.class).longValue();
  }

  private long runReservationStep() {
    var jobExecution =
        jobLauncherTestUtils.launchStep(
            HISTORICAL_ASSESSMENT_RESERVATION_STEP);
    Assertions.assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    return jobExecution.getJobId();

  }

  private long runStoreStep() {
    var jobExecution =
        jobLauncherTestUtils.launchStep(
            HISTORICAL_ASSESSMENT_STORE_STEP);
    Assertions.assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    return jobExecution.getJobId();

  }
}
