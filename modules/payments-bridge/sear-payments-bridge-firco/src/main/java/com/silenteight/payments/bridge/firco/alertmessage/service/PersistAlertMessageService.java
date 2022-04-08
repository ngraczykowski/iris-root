package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
class PersistAlertMessageService {

  private final AlertMessageRepository messageRepository;
  private final AlertMessagePayloadRepository payloadRepository;
  private final ObjectMapper objectMapper;
  private final AtomicLong hitsGuage;

  PersistAlertMessageService(
      final AlertMessageRepository messageRepository,
      final AlertMessagePayloadRepository payloadRepository,
      final ObjectMapper objectMapper,
      final MeterRegistry meterRegistry
  ) {
    this.messageRepository = messageRepository;
    this.payloadRepository = payloadRepository;
    this.objectMapper = objectMapper;

    this.hitsGuage = meterRegistry.gauge("solving.alert.hits.size", new AtomicLong(0L));
  }

  @Transactional
  @Timed(histogram = true, percentiles = { 0.5, 0.95, 0.99 })
  public void createAlertMessage(FircoAlertMessage message) {
    this.hitsGuage.set(message.getAlertMessage().getHits().size());
    messageRepository.save(new AlertMessageEntity(message));
    payloadRepository.save(convertToPayload(message));
  }

  AlertMessagePayload convertToPayload(FircoAlertMessage message) {
    var originalMessage = objectMapper.convertValue(message.getAlertMessage(), ObjectNode.class);
    return new AlertMessagePayload(message.getId(), originalMessage);
  }
}
