package com.silenteight.payments.bridge.svb.newlearning.job.unregistered;

import com.silenteight.payments.bridge.svb.newlearning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.svb.newlearning.step.unregistered.UnregisteredJobConstants.UNREGISTERED_STEP_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql
@Import({ TestApplicationConfiguration.class })
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.unregistered",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter"})
class ProcessUnregisteredAlertsJobTest extends BaseBatchTest {


  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingAlert() {
    var transformAlertStep = createStepExecution(UNREGISTERED_STEP_NAME).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(2);
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
