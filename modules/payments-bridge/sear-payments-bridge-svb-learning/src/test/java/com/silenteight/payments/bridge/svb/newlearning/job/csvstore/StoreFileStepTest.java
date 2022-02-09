package com.silenteight.payments.bridge.svb.newlearning.job.csvstore;

import com.silenteight.payments.bridge.svb.newlearning.job.TestApplicationConfiguration;
import com.silenteight.payments.bridge.svb.newlearning.step.alert.LearningAlertEntity;
import com.silenteight.payments.bridge.svb.newlearning.step.store.LearningCsvRowEntity;
import com.silenteight.payments.bridge.testing.BaseBatchTest;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Import({ TestApplicationConfiguration.class })
@Sql
@ComponentScan(basePackages = {
    "com.silenteight.payments.bridge.svb.newlearning.job.csvstore",
    "com.silenteight.payments.bridge.svb.newlearning.step",
    "com.silenteight.payments.bridge.svb.newlearning.adapter",
    "com.silenteight.payments.bridge.svb.newlearning.config",
    "com.silenteight.payments.bridge.svb.migration",
    "com.silenteight.payments.bridge.common.resource.csv.file.provider.service.def" })
public class StoreFileStepTest extends BaseBatchTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  @Sql(scripts = "StoreFileStepTest.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void shouldExecuteStoreFileStep() {
    executeJob("analystdecison-2-hits.csv", 4);
  }

  @Test
  @Sql(scripts = "StoreFileStepTestTwoFiles.sql")
  @Sql(scripts = "../TruncateJobData.sql", executionPhase = AFTER_TEST_METHOD)
  @Transactional(propagation = Propagation.SUPPORTS)
  public void testTwoFiles() {
    executeJob("analystdecison-2-hits_1.csv", 2);
    executeJob("analystdecison-2-hits_2.csv", 2);

    var resultList = testEntityManager.getEntityManager()
        .createQuery("FROM LearningCsvRow", LearningCsvRowEntity.class)
        .getResultList();

    assertThat(
        resultList.stream()
            .map(LearningCsvRowEntity::getFileName)
            .collect(Collectors.toList()))
        .containsOnly("analystdecison-2-hits_1.csv", "analystdecison-2-hits_2.csv");
  }

  private void executeJob(String fileName, int expectedRead) {
    var jobParameters = StoreStepFixture.toParams(fileName, "bucket");
    var jobExecution = jobLauncherTestUtils.launchStep(STORE_FILE_STEP, jobParameters);
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

    var firstStep = jobExecution
        .getStepExecutions()
        .stream()
        .filter(step -> STORE_FILE_STEP.equals(step.getStepName()))
        .findFirst();
    assertThat(firstStep.isPresent()).isTrue();
    assertThat(firstStep.get().getReadCount()).isEqualTo(expectedRead);
  }

  @Test
  @Transactional(propagation = Propagation.SUPPORTS)
  void end2EndStoreCsvJobWithTwoHits() throws Exception {
    var expectedFileName = "analystdecison-2-hits.csv";
    var jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
        .addString(FILE_NAME_PARAMETER, "analystdecison-2-hits.csv")
        .addString(BUCKET_NAME_PARAMETER, "bucket")
        .toJobParameters());
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

    var csvRows = testEntityManager.getEntityManager()
        .createQuery("FROM LearningCsvRow", LearningCsvRowEntity.class)
        .getResultList();

    assertThat(
        csvRows.stream()
            .map(LearningCsvRowEntity::getFileName)
            .collect(Collectors.toList()))
        .containsOnly(expectedFileName);

    var alerts = testEntityManager
        .getEntityManager()
        .createQuery("FROM LearningAlert", LearningAlertEntity.class)
        .getResultList();
    assertThat(alerts.size()).isEqualTo(2);
    assertThat(
        alerts.stream()
            .map(LearningAlertEntity::getFileName)
            .collect(Collectors.toList()))
        .containsOnly(expectedFileName);
  }
}
