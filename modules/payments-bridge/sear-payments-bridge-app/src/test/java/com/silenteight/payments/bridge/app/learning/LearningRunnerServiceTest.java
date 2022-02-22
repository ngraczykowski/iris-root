package com.silenteight.payments.bridge.app.learning;

import com.silenteight.payments.bridge.common.event.TriggerBatchJobEvent;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STORE_CSV_JOB_NAME;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningRunnerServiceTest {

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;
  @Mock
  private HandleLearningAlertsUseCase handleLearningAlertsUseCase;

  @Test
  void shouldTriggerBatchJob() {
    var properties = new LearningProperties();
    properties.setUseNewLearning(true);
    var learningRunnerService =
        new LearningRunnerService(
            properties, applicationEventPublisher, handleLearningAlertsUseCase);
    var file = LearningFileEntity.builder().fileName("fileName").bucketName("bucketName").build();
    learningRunnerService.trigger(file);
    verify(applicationEventPublisher, times(1)).publishEvent(TriggerBatchJobEvent
        .builder()
        .jobName(STORE_CSV_JOB_NAME)
        .parameters(file.toJobParameters())
        .build());
    verify(handleLearningAlertsUseCase, times(0)).readAlerts(any());
  }

  @Test
  void shouldTriggerOldLearning() {
    var properties = new LearningProperties();
    var learningRunnerService =
        new LearningRunnerService(
            properties, applicationEventPublisher, handleLearningAlertsUseCase);
    learningRunnerService.trigger(
        LearningFileEntity.builder().fileName("fileName").bucketName("bucketName").build());
    verify(applicationEventPublisher, times(0)).publishEvent(any());
    verify(handleLearningAlertsUseCase, times(1)).readAlerts(any());
  }
}
