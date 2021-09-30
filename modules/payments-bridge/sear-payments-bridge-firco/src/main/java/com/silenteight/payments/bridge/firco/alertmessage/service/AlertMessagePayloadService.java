package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class AlertMessagePayloadService implements AlertMessagePayloadUseCase {

  private final AlertMessagePayloadRepository payloadRepository;
  private final ObjectMapper objectMapper;

  @Override
  public AlertMessageDto findByAlertMessageId(UUID alertMessageId) {
    var payload = payloadRepository.findByAlertMessageId(alertMessageId).orElseThrow();
    return mapToDto(payload.getOriginalMessage());
  }

  private AlertMessageDto mapToDto(ObjectNode objectNode) {
    return objectMapper.convertValue(objectNode, AlertMessageDto.class);
  }

}
