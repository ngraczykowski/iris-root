package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.port.EtlUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@RequiredArgsConstructor
@MessageEndpoint
@Slf4j
class UniversalDataSourceEndpoint {

  private final EtlUseCase etlUseCase;

  @ServiceActivator(inputChannel = AlertRegisteredEvent.CHANNEL,
      outputChannel = AlertInputAcceptedEvent.CHANNEL)
  AlertInputAcceptedEvent apply(AlertRegisteredEvent command) {
    etlUseCase.process(command);
    return new AlertInputAcceptedEvent(command.getAeAlert());
  }

}
