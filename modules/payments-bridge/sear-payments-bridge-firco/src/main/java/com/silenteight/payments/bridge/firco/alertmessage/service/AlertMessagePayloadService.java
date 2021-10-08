package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.UUID;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
class AlertMessagePayloadService implements AlertMessagePayloadUseCase {

  private final AlertMessagePayloadRepository payloadRepository;
  private final ObjectMapper objectMapper;

  @Override
  public AlertMessageDto findByAlertMessageId(UUID alertMessageId) {
    var payload = payloadRepository.findByAlertMessageId(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);
    return mapToDto(payload.getOriginalMessage());
  }

  private AlertMessageDto mapToDto(ObjectNode objectNode) {
    return objectMapper.convertValue(objectNode, AlertMessageDto.class);
  }

}
