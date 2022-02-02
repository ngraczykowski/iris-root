package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.NotificationEvent;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.port.LearningCsvNotificationCreatorUseCase;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.svb.learning.reader.service.LearningAlertsConfig.LEARNING_THREAD_POOL_EXECUTOR;

@Service
@RequiredArgsConstructor
@Slf4j
class HandleLearningAlertsService implements HandleLearningAlertsUseCase {

  private final ProcessAlertService processAlertService;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final LearningCsvNotificationCreatorUseCase learningCsvNotificationCreatorUseCase;

  @Async(LEARNING_THREAD_POOL_EXECUTOR)
  public void readAlerts(LearningRequest learningRequest) {
    log.info("Started processing learn request: {}", learningRequest);
    var alertsRead = processAlertService.read(learningRequest);

    var notification = learningCsvNotificationCreatorUseCase.createLearningCsvNotification(
        alertsRead.toLearningCsvNotificationRequest());
    applicationEventPublisher.publishEvent(new NotificationEvent(notification));

    log.info("Processing of learn request {} finished", learningRequest);
  }
}
