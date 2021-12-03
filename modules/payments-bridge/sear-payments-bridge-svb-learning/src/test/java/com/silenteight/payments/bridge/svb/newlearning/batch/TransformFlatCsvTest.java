package com.silenteight.payments.bridge.svb.newlearning.batch;


import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.batch.step.action.LearningActionEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.action.LearningActionRepository;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.alert.LearningAlertEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.alert.LearningAlertRepository;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.hit.LearningHitEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.hit.LearningHitRepository;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.listrecord.LearningListedRecordEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.listrecord.LearningRecordRepository;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.message.LearningAlertedMessageEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.message.LearningAlertedMessageRepository;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.status.LearningActionStatusEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.status.LearningActionStatusRepository;
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

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SuppressWarnings({
    "SpringJavaInjectionPointsAutowiringInspection", "OptionalGetWithoutIsPresent" })
@Import({ TestApplicationConfiguration.class })
@Slf4j
@ComponentScan(basePackages = "com.silenteight.payments.bridge.svb.newlearning")
class TransformFlatCsvTest extends BaseBatchTest {

  @Autowired
  private LearningAlertRepository learningAlertRepository;
  @Autowired
  private LearningActionRepository learningActionRepository;
  @Autowired
  private LearningHitRepository learningHitRepository;
  @Autowired
  private LearningRecordRepository learningRecordRepository;
  @Autowired
  private LearningActionStatusRepository learningActionStatusRepository;
  @Autowired
  private LearningAlertedMessageRepository learningAlertedMessageRepository;


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
    assertThat(transformHitStep.get().getReadCount()).isEqualTo(2);

    var savedCount = ((Collection<LearningHitEntity>) learningHitRepository.findAll()).size();
    assertThat(savedCount).isEqualTo(2);
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

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingStatus() {
    var transformStatusStep = createStepExecution(STEP_TRANSFORM_STATUS);
    assertThat(transformStatusStep.isPresent()).isTrue();
    assertThat(transformStatusStep.get().getReadCount()).isEqualTo(2);

    var savedCount =
        ((Collection<LearningActionStatusEntity>) learningActionStatusRepository.findAll()).size();
    assertThat(savedCount).isEqualTo(2);
  }

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingAlertedMessage() {
    var transformAlertedMessageStep = createStepExecution(STEP_TRANSFORM_ALERTED_MESSAGE);
    assertThat(transformAlertedMessageStep.isPresent()).isTrue();
    assertThat(transformAlertedMessageStep.get().getReadCount()).isEqualTo(2);

    var savedCount =
        ((Collection<LearningAlertedMessageEntity>)
            learningAlertedMessageRepository.findAll()).size();

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
