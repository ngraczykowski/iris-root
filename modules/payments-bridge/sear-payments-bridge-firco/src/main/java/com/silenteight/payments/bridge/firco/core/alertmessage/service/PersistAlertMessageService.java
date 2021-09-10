package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class PersistAlertMessageService {

  private final AlertMessageRepository messageRepository;
  private final AlertMessagePayloadFactory payloadFactory;
  private final AlertMessagePayloadRepository payloadRepository;

  @Transactional
  public void createAlertMessage(FircoAlertMessage alertMessage) {
    messageRepository.save(new AlertMessageEntity(alertMessage));
    payloadRepository.save(payloadFactory.create(alertMessage));
  }
}
