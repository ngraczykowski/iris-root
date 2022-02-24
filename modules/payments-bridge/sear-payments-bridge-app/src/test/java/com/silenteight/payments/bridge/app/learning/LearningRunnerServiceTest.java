package com.silenteight.payments.bridge.app.learning;

import com.silenteight.payments.bridge.common.event.TriggerBatchJobEvent;

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

  @Test
  void shouldTriggerBatchJob() {
    var learningRunnerService =
        new LearningRunnerService(applicationEventPublisher);
    var file = LearningFileEntity.builder().fileName("fileName").bucketName("bucketName").build();
    learningRunnerService.trigger(file);
    verify(applicationEventPublisher, times(1)).publishEvent(TriggerBatchJobEvent
        .builder()
        .jobName(STORE_CSV_JOB_NAME)
        .parameters(file.toJobParameters())
        .build());
  }

  @Test
  void shouldTriggerOldLearning() {
    var learningRunnerService =
        new LearningRunnerService(applicationEventPublisher);
    learningRunnerService.trigger(
        LearningFileEntity.builder().fileName("fileName").bucketName("bucketName").build());
    verify(applicationEventPublisher, times(0)).publishEvent(any());
  }
}
