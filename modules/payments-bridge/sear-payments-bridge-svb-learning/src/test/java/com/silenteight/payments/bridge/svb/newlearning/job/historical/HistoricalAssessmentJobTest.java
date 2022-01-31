package com.silenteight.payments.bridge.svb.newlearning.job.historical;

import com.silenteight.payments.bridge.svb.newlearning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_ASSESSMENT_STORE_STEP;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.historical",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter",
    "com.silenteight.payments.bridge.svb.newlearning.config",
    "com.silenteight.payments.bridge.svb.migration" })
class HistoricalAssessmentJobTest extends BaseBatchTest {

  @Autowired
  private Job historicalRiskAssessmentJob;

  @Test
  @Sql(scripts = "HistoricalAssessmentReservationStepTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void shouldRunStoreStep() throws JobInstanceAlreadyCompleteException,
      JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

    var jobExecution = jobLauncherTestUtils
        .getJobLauncher()
        .run(historicalRiskAssessmentJob, new JobParametersBuilder().toJobParameters());
    var step = jobExecution
        .getStepExecutions()
        .stream()
        .filter(s -> HISTORICAL_ASSESSMENT_STORE_STEP.equals(s.getStepName())).findFirst();
    Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    Assertions.assertThat(step.isPresent()).isEqualTo(true);
    Assertions.assertThat(step.get().getReadCount()).isEqualTo(3);
    Assertions.assertThat(step.get().getWriteCount()).isEqualTo(3);
  }


}
