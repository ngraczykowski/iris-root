package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

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
  @Timed(histogram = true, percentiles = { 0.5, 0.95, 0.99 })
  public AlertMessageDto findByAlertMessageId(UUID alertMessageId) {
    var payload = payloadRepository.findByAlertMessageId(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);
    return mapToDto(payload.getOriginalMessage());
  }

  private AlertMessageDto mapToDto(ObjectNode objectNode) {
    return objectMapper.convertValue(objectNode, AlertMessageDto.class);
  }

}
