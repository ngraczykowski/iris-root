package com.silenteight.payments.bridge.svb.learning.job.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.NotificationEvent;
import com.silenteight.payments.bridge.svb.learning.port.LearningCsvNotificationCreatorUseCase;
import com.silenteight.payments.bridge.svb.learning.port.LearningDataAccess;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;

@Service
@Slf4j
@RequiredArgsConstructor
class EtlAlertsJobListener implements JobExecutionListener {

  private final LearningDataAccess learningDataAccess;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final LearningCsvNotificationCreatorUseCase learningCsvNotificationCreatorUseCase;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    var fileName = jobExecution.getJobParameters().getString(FILE_NAME_PARAMETER);
    log.info("Starting batch processing for file = {}", fileName);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    var fileName = jobExecution.getJobParameters().getString(FILE_NAME_PARAMETER);
    var jobId = jobExecution.getJobId();

    var result = learningDataAccess.select(jobId, fileName);

    log.info(
        "Finished processing file = {} with {} successfully processed alerts and {} failed",
        fileName,
        result.getSuccessfulAlerts(),
        result.getFailedAlerts());

    var notification = learningCsvNotificationCreatorUseCase.createLearningCsvNotification(
        result.toLearningCsvNotificationRequest());
    applicationEventPublisher.publishEvent(new NotificationEvent(notification));
  }
}
