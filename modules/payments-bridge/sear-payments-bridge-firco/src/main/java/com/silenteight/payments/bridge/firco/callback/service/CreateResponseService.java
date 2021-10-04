package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.callback.port.CreateResponseUseCase;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
class CreateResponseService implements CreateResponseUseCase {

  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final CallbackRequestFactory callbackRequestFactory;
  private final ClientRequestDtoMapper mapper;

  @Override
  public void createResponse(UUID alertId, AlertMessageStatus status) {
    var alert = alertMessageUseCase
        .findByAlertMessageId(alertId.toString());
    var alertDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);
    var requestDto = mapper.mapToAlertDecision(alert, alertDto, status);
    callbackRequestFactory.create(requestDto).invoke();
  }
}
