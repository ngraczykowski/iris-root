package com.silenteight.payments.bridge.svb.newlearning.job.csvstore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.StopBatchJobEvent;
import com.silenteight.payments.bridge.common.event.TriggerBatchJobEvent;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.newlearning.job.etl.EtlJobConstants.ETL_JOB_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
class StoreCsvJobListener implements JobExecutionListener {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final LearningDataAccess learningDataAccess;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    var fileName = jobExecution.getJobParameters().getString(FILE_NAME_PARAMETER);

    if (!learningDataAccess.isFileStored(fileName)) {
      log.info("Starting batch store csv job for file = {}", fileName);
      return;
    }

    log.warn("Skipping store job for file that is already stored = {}", fileName);
    applicationEventPublisher.publishEvent(
        StopBatchJobEvent.builder().jobExecutionId(jobExecution.getId()).build());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    var fileName = jobExecution.getJobParameters().getString(FILE_NAME_PARAMETER);

    if (fileName == null) {
      log.error(FILE_NAME_PARAMETER + " not set. Can't run csv processing job");
      return;
    }

    log.info("Triggering batch process csv job for file = {}", fileName);

    applicationEventPublisher.publishEvent(
        TriggerBatchJobEvent.builder().jobName(ETL_JOB_NAME).parameters(new JobParametersBuilder()
            .addString(FILE_NAME_PARAMETER, fileName)
            .toJobParameters()).build());
  }
}
