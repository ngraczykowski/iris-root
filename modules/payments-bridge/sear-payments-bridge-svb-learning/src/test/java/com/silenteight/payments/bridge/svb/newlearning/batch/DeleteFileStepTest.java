package com.silenteight.payments.bridge.svb.newlearning.batch;

import com.silenteight.payments.bridge.svb.newlearning.batch.step.store.LearningCsvRowRepository;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobParameters.FILE_ID_PARAMETER;
import static com.silenteight.payments.bridge.svb.newlearning.batch.step.delete.DeleteCsvFileTaskletConfiguration.DELETE_FILE_STEP;
import static org.assertj.core.api.Assertions.*;

@Import({ TestApplicationConfiguration.class })
@Sql
public class DeleteFileStepTest extends BaseBatchTest {

  @Autowired
  private LearningCsvRowRepository repository;

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void shouldExecuteDeleteFileStep() {
    var jobParameters = new JobParametersBuilder()
        .addLong(FILE_ID_PARAMETER, 345L)
        .toJobParameters();
    var jobExecution = jobLauncherTestUtils.launchStep(DELETE_FILE_STEP, jobParameters);
    assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
    var firstStep = jobExecution
        .getStepExecutions()
        .stream().filter(step -> DELETE_FILE_STEP.equals(step.getStepName())).findFirst();
    assertThat(firstStep.isPresent()).isTrue();
  }
}
