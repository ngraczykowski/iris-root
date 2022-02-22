package com.silenteight.payments.bridge.app.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.TriggerBatchJobEvent;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STORE_CSV_JOB_NAME;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(LearningProperties.class)
@Slf4j
class LearningRunnerService {

  private final LearningProperties properties;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final HandleLearningAlertsUseCase handleLearningAlertsUseCase;

  void trigger(LearningFileEntity file) {
    log.info("Trigger learning of a file: {}", file.getFileName());
    if (properties.getUseNewLearning()) {
      applicationEventPublisher.publishEvent(
          TriggerBatchJobEvent
              .builder()
              .jobName(STORE_CSV_JOB_NAME)
              .parameters(file.toJobParameters())
              .build());
      return;
    }

    handleLearningAlertsUseCase.readAlerts(file.toLearningRequest());
  }
}
