package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.ResponsePublisherPort;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
class ResponseGeneratorService {

  private final ResponsePublisherPort responsePublisherPort;
  private final AlertMessageRepository alertMessageRepository;
  private final AlertMessagePayloadRepository payloadRepository;
  private final AlertDecisionMapper alertDecisionMapper;

  void prepareAndSendResponse(UUID alertMessageId, AlertMessageStatus status) {
    var alertMessage = alertMessageRepository
        .findById(alertMessageId).orElseThrow(EntityNotFoundException::new);
    var payload = payloadRepository.findByAlertMessageId(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);
    var alertDecision = alertDecisionMapper
        .mapToAlertDecision(alertMessage, payload, status);
    responsePublisherPort.send(alertDecision);
  }

}
