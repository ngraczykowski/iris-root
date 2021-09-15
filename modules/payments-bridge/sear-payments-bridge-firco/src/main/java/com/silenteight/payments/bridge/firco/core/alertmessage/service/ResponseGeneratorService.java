package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.ResponsePublisherPort;

import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
class ResponseGeneratorService {

  private final ResponsePublisherPort responsePublisherPort;

  void prepareAndSendResponse(UUID alertMessageId, AlertMessageStatus status) {
    responsePublisherPort.send(alertMessageId);
  }

}
