package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class AlertMessagePayloadFactory {

  private final ObjectMapper objectMapper;

  AlertMessagePayload create(FircoAlertMessage message) {
    var originalMessage = objectMapper.convertValue(message.getAlertMessage(), ObjectNode.class);
    return new AlertMessagePayload(message.getId(), originalMessage);
  }
}
