package com.silenteight.payments.bridge.app.learning;

import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningRunnerServiceTest {

  @Mock
  private JobMaintainer jobMaintainer;
  @Mock
  private HandleLearningAlertsUseCase handleLearningAlertsUseCase;

  @Test
  void shouldTriggerBatchJob() {
    var properties = new LearningProperties();
    properties.setUseNewLearning(true);
    var learningRunnerService =
        new LearningRunnerService(properties, jobMaintainer, handleLearningAlertsUseCase);
    learningRunnerService.trigger(
        LearningFileEntity.builder().fileName("fileName").bucketName("bucketName").build());
    verify(jobMaintainer, times(1)).runJobByName(any(), any());
    verify(handleLearningAlertsUseCase, times(0)).readAlerts(any());
  }

  @Test
  void shouldTriggerOldLearning() {
    var properties = new LearningProperties();
    var learningRunnerService =
        new LearningRunnerService(properties, jobMaintainer, handleLearningAlertsUseCase);
    learningRunnerService.trigger(
        LearningFileEntity.builder().fileName("fileName").bucketName("bucketName").build());
    verify(jobMaintainer, times(0)).runJobByName(any(), any());
    verify(handleLearningAlertsUseCase, times(1)).readAlerts(any());
  }
}
