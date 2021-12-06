package com.silenteight.payments.bridge.svb.newlearning.batch;


import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.batch.step.action.LearningActionEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.alert.LearningAlertEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.hit.LearningHitEntity;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.listrecord.LearningListedRecordEntity;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.STEP_TRANSFORM_ALERT;
import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.STEP_TRANSFORM_HIT;
import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.STEP_TRANSFORM_RECORD;
import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.TRANSFORM_ACTION_STEP;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "unchecked" })
@Import({ TestApplicationConfiguration.class })
@Slf4j
@ComponentScan(basePackages = "com.silenteight.payments.bridge.svb.newlearning")
class TransformFlatCsvTest extends BaseBatchTest {

  @Autowired
  private EntityManager entityManager;

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingAlert() {
    var transformAlertStep = createStepExecution(STEP_TRANSFORM_ALERT).get();
    assertThat(transformAlertStep.getReadCount()).isEqualTo(3);

    Query query = entityManager.createQuery("SELECT a FROM LearningAlert a");
    var savedCount = ((Collection<LearningAlertEntity>) query.getResultList()).size();
    assertThat(savedCount).isEqualTo(3);
  }

  @Test
  @Sql(scripts = "TransformFlatCsvTest.sql")
  @Sql(scripts = "TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public void testTransformingAction() {
    var transformActionStep = createStepExecution(TRANSFORM_ACTION_STEP).get();
    assertThat(transformActionStep.getReadCount()).isEqualTo(4);

    Query query = entityManager.createQuery("SELECT a FROM LearningAction a");
    var savedCount = ((Collection<LearningActionEntity>) query.getResultList()).size();
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

    Query query = entityManager.createQuery("SELECT a FROM LearningHit a");
    var savedCount = ((Collection<LearningHitEntity>) query.getResultList()).size();
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

    Query query = entityManager.createQuery("SELECT a FROM LearningListedRecord a");
    var savedCount = ((Collection<LearningListedRecordEntity>) query.getResultList()).size();
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
