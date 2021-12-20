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

@Import({ TestApplicationConfiguration.class })
@Sql
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.historical",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter" })
class HistoricalAssessmentReservationStepTest extends BaseBatchTest {

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void alertReservationStepTest() {
    var jobExecution =
        jobLauncherTestUtils.launchStep(
            HISTORICAL_ASSESSMENT_RESERVATION_STEP);
    Assertions.assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    var step = jobExecution
        .getStepExecutions()
        .stream()
        .filter(s -> HISTORICAL_ASSESSMENT_RESERVATION_STEP.equals(s.getStepName()))
        .findFirst();
    Assertions.assertThat(step.get().getWriteCount()).isEqualTo(3);
  }
}
