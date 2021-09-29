package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class AlertMessageService implements AlertMessageUseCase {

  private final AlertMessageRepository alertMessageRepository;

  public AlertMessageModel findByAlertMessageId(String alertMessageId) {
    var entity = alertMessageRepository.findById(UUID.fromString(alertMessageId))
        .orElseThrow();

    // TODO: incorporate eg. mapstruct mapper to avoid such mess.
    return AlertMessageModel.builder()
        .messageId(entity.getMessageId())
        .businessUnit(entity.getBusinessUnit())
        .dataCenter(entity.getDataCenter())
        .decisionUrl(entity.getDecisionUrl())
        .priority(entity.getPriority())
        .receivedAt(entity.getReceivedAt())
        .systemId(entity.getSystemId())
        .id(entity.getId())
        .unit(entity.getUnit()).build();
  }

}
