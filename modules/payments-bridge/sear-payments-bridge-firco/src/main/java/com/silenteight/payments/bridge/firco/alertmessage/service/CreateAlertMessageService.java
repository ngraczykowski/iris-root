package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.metrics.alert.AlertResolutionStartEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@Slf4j
class CreateAlertMessageService implements CreateAlertMessageUseCase {

  private final PersistAlertMessageService persistAlertMessageService;
  private final AlertMessageStatusService alertMessageStatusService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Transactional
  @Override
  public void createAlertMessage(FircoAlertMessage message) {
    log.info("Creating alert [{}] from message: [{}]", message.getId(),
        message.getAlertMessage().getMessageID());
    applicationEventPublisher.publishEvent(new AlertResolutionStartEvent(message.getId(),
        Instant.from(message.getReceivedAt()).toEpochMilli()));
    persistAlertMessageService.createAlertMessage(message);
    alertMessageStatusService.initState(message.getId());
    applicationEventPublisher.publishEvent(message);
  }

}
