package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.NotificationEvent;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;

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

  @Async(LEARNING_THREAD_POOL_EXECUTOR)
  public void readAlerts(LearningRequest learningRequest) {
    log.info("Started processing learn request: {}", learningRequest);
    var alertsRead = processAlertService.read(learningRequest);

    var notificationEvent = new NotificationEvent(alertsRead.toNotification());
    applicationEventPublisher.publishEvent(notificationEvent);
    log.info("Processing of learn request {} finished", learningRequest);
  }
}
