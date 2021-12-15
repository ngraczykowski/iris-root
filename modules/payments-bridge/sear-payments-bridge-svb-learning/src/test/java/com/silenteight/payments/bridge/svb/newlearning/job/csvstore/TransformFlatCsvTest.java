package com.silenteight.payments.bridge.svb.newlearning.job.csvstore;


import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.svb.newlearning.step.action.LearningActionEntity;
import com.silenteight.payments.bridge.svb.newlearning.step.action.LearningActionRepository;
import com.silenteight.payments.bridge.svb.newlearning.step.alert.LearningAlertEntity;
import com.silenteight.payments.bridge.svb.newlearning.step.alert.LearningAlertRepository;
import com.silenteight.payments.bridge.svb.newlearning.step.hit.LearningHitEntity;
import com.silenteight.payments.bridge.svb.newlearning.step.hit.LearningHitRepository;
import com.silenteight.payments.bridge.svb.newlearning.step.listrecord.LearningListedRecordEntity;
import com.silenteight.payments.bridge.svb.newlearning.step.listrecord.LearningRecordRepository;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STEP_TRANSFORM_ALERT;
import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STEP_TRANSFORM_HIT;
import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STEP_TRANSFORM_RECORD;
import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.TRANSFORM_ACTION_STEP;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SuppressWarnings({
    "SpringJavaInjectionPointsAutowiringInspection", "OptionalGetWithoutIsPresent" })
@Import({ TestApplicationConfiguration.class })
@Slf4j
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.csvstore",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter"})
class TransformFlatCsvTest extends BaseBatchTest {

  @Autowired
  private LearningAlertRepository learningAlertRepository;
  @Autowired
  private LearningActionRepository learningActionRepository;
  @Autowired
  private LearningHitRepository learningHitRepository;
  @Autowired
  private LearningRecordRepository learningRecordRepository;

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingAlert() {
    var transformAlertStep = createStepExecution(STEP_TRANSFORM_ALERT).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(3);

    var savedCount = ((Collection<LearningAlertEntity>) learningAlertRepository.findAll()).size();
    assertThat(savedCount).isEqualTo(3);
  }

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingAction() {
    var transformActionStep = createStepExecution(TRANSFORM_ACTION_STEP).get();
    assertThat(transformActionStep.getReadCount()).isEqualTo(4);

    var savedCount = ((Collection<LearningActionEntity>) learningActionRepository.findAll()).size();
    assertThat(savedCount).isEqualTo(4);
  }

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingHit() {
    var transformHitStep = createStepExecution(STEP_TRANSFORM_HIT);
    assertThat(transformHitStep.isPresent()).isTrue();
    assertThat(transformHitStep.get().getReadCount()).isEqualTo(1);

    var savedCount = ((Collection<LearningHitEntity>) learningHitRepository.findAll()).size();
    assertThat(savedCount).isEqualTo(1);
  }

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingListedRecord() {
    var transformListedRecordStep = createStepExecution(STEP_TRANSFORM_RECORD);
    assertThat(transformListedRecordStep.isPresent()).isTrue();
    assertThat(transformListedRecordStep.get().getReadCount()).isEqualTo(2);

    var savedCount =
        ((Collection<LearningListedRecordEntity>) learningRecordRepository.findAll()).size();
    assertThat(savedCount).isEqualTo(2);
  }

  @Nonnull
  private Optional<StepExecution> createStepExecution(String stepName) {
    var jobParameters = new JobParametersBuilder()
        .addLong("fileId", 1L)
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
