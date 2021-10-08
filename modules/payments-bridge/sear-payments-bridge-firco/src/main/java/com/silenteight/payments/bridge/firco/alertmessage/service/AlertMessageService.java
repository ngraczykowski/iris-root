package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class AlertMessageService implements AlertMessageUseCase {

  private final AlertMessageRepository alertMessageRepository;

  public AlertData findByAlertMessageId(UUID alertMessageId) {
    var entity = alertMessageRepository.findById(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);

    // TODO: incorporate eg. mapstruct mapper to avoid such mess.
    return AlertData.builder()
        .messageId(entity.getMessageId())
        .businessUnit(entity.getBusinessUnit())
        .dataCenter(entity.getDataCenter())
        .decisionUrl(entity.getDecisionUrl())
        .priority(entity.getPriority())
        .receivedAt(entity.getReceivedAt())
        .systemId(entity.getSystemId())
        .alertId(entity.getId())
        .unit(entity.getUnit()).build();
  }

}
