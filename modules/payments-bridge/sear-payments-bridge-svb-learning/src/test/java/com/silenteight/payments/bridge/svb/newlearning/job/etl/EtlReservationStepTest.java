package com.silenteight.payments.bridge.svb.newlearning.job.etl;

import com.silenteight.payments.bridge.svb.newlearning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.StoreStepFixture;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.svb.newlearning.job.etl.EtlJobConstants.ETL_RESERVATION_STEP;
import static com.silenteight.payments.bridge.svb.newlearning.job.etl.EtlJobConstants.ETL_STEP_NAME;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.etl",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter",
    "com.silenteight.payments.bridge.svb.newlearning.config",
    "com.silenteight.payments.bridge.svb.newlearning.service",
    "com.silenteight.payments.bridge.svb.migration",
    "com.silenteight.payments.bridge.common.resource.csv.file.provider.service.def"
})
class EtlReservationStepTest extends BaseBatchTest {


  @Test
  @Sql(scripts = "EtlReservationStepTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void shouldRunReservationStepTwice() {
    var jobId = runReservationStep("bucket");
    var count = fetchReservedForJob(jobId);
    Assertions.assertThat(count).isEqualTo(3);
    // Second time on conflict do nothing should not put any data so result should be the same.
    var secondJobId = runReservationStep("bucket1");

    var reservedFirstJob = fetchReservedForJob(jobId);
    var reservedSecondJob = fetchReservedForJob(secondJobId);
    // On second reservation execution it should already been assigned to first execution only.
    Assertions.assertThat(reservedFirstJob).isEqualTo(3);
    Assertions.assertThat(reservedSecondJob).isEqualTo(3);
  }

  private long fetchReservedForJob(long jobId) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM pb_learning_etl_reservation WHERE job_id=" + jobId,
        Number.class).longValue();
  }

  private long runReservationStep(String bucket) {
    var jobExecution =
        jobLauncherTestUtils.launchStep(
            ETL_RESERVATION_STEP, StoreStepFixture.toParams("analystdecison-2-hits.csv", bucket));
    Assertions.assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    return jobExecution.getJobId();

  }

  private long runStoreStep() {
    var jobExecution =
        jobLauncherTestUtils.launchStep(
            ETL_STEP_NAME);
    Assertions.assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    return jobExecution.getJobId();

  }
}
