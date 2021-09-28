package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class PersistAlertMessageService {

  private final AlertMessageRepository messageRepository;
  private final AlertMessagePayloadRepository payloadRepository;

  @Transactional
  public void createAlertMessage(AlertMessageModel alertMessage, ObjectNode originalMessage) {
    messageRepository.save(new AlertMessageEntity(alertMessage));
    payloadRepository.save(new AlertMessagePayload(alertMessage.getId(), originalMessage));
  }

}
