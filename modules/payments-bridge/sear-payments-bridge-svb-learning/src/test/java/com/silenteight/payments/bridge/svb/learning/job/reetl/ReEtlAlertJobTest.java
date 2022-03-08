package com.silenteight.payments.bridge.svb.learning.job.reetl;


import com.silenteight.payments.bridge.svb.learning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

import static com.silenteight.payments.bridge.svb.learning.job.reetl.ReEtlJobConstants.RE_ETL_STEP_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.learning.job.reetl",
    "com.silenteight.payments.bridge.svb.learning.step",
    "com.silenteight.payments.bridge.svb.learning.adapter",
    "com.silenteight.payments.bridge.svb.learning.config",
    "com.silenteight.payments.bridge.svb.learning.service",
    "com.silenteight.payments.bridge.svb.learning.mapping",
    "com.silenteight.payments.bridge.common.resource.csv.file.provider.service.def" })
class ReEtlAlertJobTest extends BaseBatchTest {

  @Test
  @DisplayName("Check data are correct processing during etl ")
  @Sql(scripts = "ReEtlAlertStepTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void checkDataAreCorrectProcessingDuringEtl() {
    var transformAlertStep = createStepExecution(RE_ETL_STEP_NAME).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(2);
  }


  @Nonnull
  private Optional<StepExecution> createStepExecution(String stepName) {
    var jobParameters = new JobParametersBuilder()
        .addString("file-name", "fileName")
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
