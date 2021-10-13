package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    log.info("Creating alert [{}] from message: [{}]",
        message.getId(), message.getAlertMessage().getMessageID());
    persistAlertMessageService.createAlertMessage(message);
    alertMessageStatusService.initState(message.getId());
    applicationEventPublisher.publishEvent(message);
  }

}
