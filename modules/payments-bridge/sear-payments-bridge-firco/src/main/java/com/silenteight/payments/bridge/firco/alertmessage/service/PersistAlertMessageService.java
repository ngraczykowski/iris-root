package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class PersistAlertMessageService {

  private final AlertMessageRepository messageRepository;
  private final AlertMessagePayloadRepository payloadRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  @Timed(histogram = true, percentiles = { 0.5, 0.95, 0.99})
  public void createAlertMessage(FircoAlertMessage message) {
    messageRepository.save(new AlertMessageEntity(message));
    payloadRepository.save(convertToPayload(message));
  }

  AlertMessagePayload convertToPayload(FircoAlertMessage message) {
    var originalMessage = objectMapper.convertValue(message.getAlertMessage(), ObjectNode.class);
    return new AlertMessagePayload(message.getId(), originalMessage);
  }
}
