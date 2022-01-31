package com.silenteight.payments.bridge.svb.newlearning.job.csvstore;

import com.silenteight.payments.bridge.svb.newlearning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.svb.newlearning.step.store.LearningCsvRowRepository;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.svb.newlearning.step.delete.DeleteCsvFileTaskletConfiguration.DELETE_FILE_STEP;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@Sql
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.csvstore",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter",
    "com.silenteight.payments.bridge.svb.newlearning.config",
    "com.silenteight.payments.bridge.svb.migration" })
public class DeleteFileStepTest extends BaseBatchTest {

  @Autowired
  private LearningCsvRowRepository repository;

  @Test
  @Sql(scripts = "DeleteFileStepTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void shouldExecuteDeleteFileStep() {
    var jobParameters =
        StoreStepFixture.toParams("analystdecison-2-hits.csv", "bucket");
    var jobExecution = jobLauncherTestUtils.launchStep(DELETE_FILE_STEP, jobParameters);
    assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    var firstStep = jobExecution
        .getStepExecutions()
        .stream().filter(step -> DELETE_FILE_STEP.equals(step.getStepName())).findFirst();
    assertThat(firstStep.isPresent()).isTrue();
  }
}
