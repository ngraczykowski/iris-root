package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.AlertInitializedEvent;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_INITIALIZED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class RegisterAlertEndpoint {

  private final RegisterAlertUseCase registerAlertUseCase;
  private final RegisteredAlertDataAccessPort registeredAlertDataAccessPort;

  @ServiceActivator(inputChannel = ALERT_INITIALIZED, outputChannel = ALERT_REGISTERED)
  AlertRegisteredEvent apply(AlertInitializedEvent alertInitializedEvent) {
    AlertData alertData = alertInitializedEvent.getData(AlertData.class);
    AlertMessageDto alertDto = alertInitializedEvent.getData(AlertMessageDto.class);

    var request = RegisterAlertRequest.builder()
        .alertId(alertData.getAlertId().toString())
        .priority(alertData.getPriority())
        .matchIds(getMatchIds(alertDto))
        .build();

    var alert = registerAlertUseCase.register(request);

    UUID alertId = UUID.fromString(request.getAlertId());

    registeredAlertDataAccessPort.save(alertId, alert.getAlertName());

    return new AlertRegisteredEvent(
        UUID.fromString(alert.getAlertId()), alert.getAlertName(),
        alert.getMatchResponsesAsMap());
  }

  private static List<String> getMatchIds(AlertMessageDto alertDto) {
    return alertDto.getHits()
        .stream()
        .map(hit -> hit.getHit().getHittedEntity().getId())
        .distinct()
        .collect(Collectors.toList());
  }

}
