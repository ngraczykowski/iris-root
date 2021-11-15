package com.silenteight.payments.bridge.app.integration.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterMatchResponse;
import com.silenteight.payments.bridge.ae.alertregistration.domain.SaveRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.event.AlertInitializedEvent;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static java.util.stream.Collectors.toList;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class RegisterAlertEndpoint {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;
  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;

  @ServiceActivator(inputChannel = AlertInitializedEvent.CHANNEL,
      outputChannel = AlertRegisteredEvent.CHANNEL)
  AlertRegisteredEvent apply(AlertInitializedEvent event) {
    var alertData = alertMessageUseCase.findByAlertMessageId(event.getAlertId());
    var alertMessageDto = alertMessagePayloadUseCase.findByAlertMessageId(event.getAlertId());

    var alert = registerAlertUseCase.register(alertData, alertMessageDto);
    registeredAlertDataAccessPort.save(SaveRegisteredAlertRequest
        .builder()
        .alertId(alertData.getAlertId())
        .alertName(alert.getAlertName())
        .matchNames(alert
            .getMatchResponses()
            .stream()
            .map(RegisterMatchResponse::getMatchName)
            .collect(toList()))
        .build());

    return new AlertRegisteredEvent(
        AeAlert.builder()
            .alertId(event.getAlertId())
            .alertName(alert.getAlertName())
            .matches(alert.getMatchResponsesAsMap()).build());
  }

}
