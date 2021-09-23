package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.CreateAlertMessageUseCase;

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
  public void createAlertMessage(FircoAlertMessage alertMessage) {
    persistAlertMessageService.createAlertMessage(alertMessage);
    alertMessageStatusService.initState(alertMessage.getId());
    applicationEventPublisher.publishEvent(alertMessage);
  }

}
