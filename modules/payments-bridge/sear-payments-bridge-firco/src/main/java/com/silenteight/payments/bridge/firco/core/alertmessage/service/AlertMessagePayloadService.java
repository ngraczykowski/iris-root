package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.port.AlertMessagePayloadUseCase;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class AlertMessagePayloadService implements AlertMessagePayloadUseCase {

  private final AlertMessagePayloadRepository payloadRepository;

  @Override
  public ObjectNode findByAlertMessageId(String alertMessageId) {
    var payload = payloadRepository.findByAlertMessageId(UUID.fromString(alertMessageId))
        .orElseThrow();
    return payload.getOriginalMessage();
  }
}
