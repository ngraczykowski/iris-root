package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
  public void createAlertMessage(AlertMessageModel alertMessageModel, ObjectNode originalMessage) {
    persistAlertMessageService.createAlertMessage(alertMessageModel, originalMessage);
    alertMessageStatusService.initState(alertMessageModel.getId());
    applicationEventPublisher.publishEvent(alertMessageModel);
  }

}
