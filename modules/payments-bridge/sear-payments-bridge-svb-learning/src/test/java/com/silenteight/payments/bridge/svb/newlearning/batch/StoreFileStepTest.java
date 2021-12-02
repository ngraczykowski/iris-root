package com.silenteight.payments.bridge.svb.newlearning.batch;

import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobParameters.FILE_ID_PARAMETER;
import static com.silenteight.payments.bridge.svb.newlearning.batch.step.store.StoreCsvFileStepConfiguration.STORE_FILE_STEP;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@Sql
public class StoreFileStepTest extends BaseBatchTest {

  @Test
  @Sql(scripts = "StoreFileStepTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void shouldExecuteStoreFileStep() {
    var jobParameters = new JobParametersBuilder()
        .addLong(FILE_ID_PARAMETER, 123L)
        .toJobParameters();
    var jobExecution = jobLauncherTestUtils.launchStep(STORE_FILE_STEP, jobParameters);
    assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    var firstStep = jobExecution
        .getStepExecutions()
        .stream().filter(step -> STORE_FILE_STEP.equals(step.getStepName())).findFirst();
    assertThat(firstStep.isPresent()).isTrue();
    assertThat(firstStep.get().getReadCount()).isEqualTo(4);
    // Use TestEntityManager. Or fix that asserts,
    //assertThat(repository.findAll().size()).isEqualTo(4);
  }
}
