package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.ResponsePublisherPort;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
class ResponseGeneratorService {

  private final ResponsePublisherPort responsePublisherPort;
  private final AlertMessageRepository alertMessageRepository;
  private final AlertDecisionMapper alertDecisionMapper = new AlertDecisionMapper();

  void prepareAndSendResponse(UUID alertMessageId, AlertMessageStatus status) {
    var alertMessage = alertMessageRepository
        .findById(alertMessageId).orElseThrow(EntityNotFoundException::new);
    var alertDecision = alertDecisionMapper.mapToAlertDecision(alertMessage, status);
    responsePublisherPort.send(alertDecision);
  }

}
