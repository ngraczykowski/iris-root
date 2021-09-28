package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.event.AlertRegistered;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.firco.datasource.port.EtlUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED;

@RequiredArgsConstructor
@MessageEndpoint
class EtlService implements EtlUseCase {

  private final List<EtlProcess> processes;

  @ServiceActivator(inputChannel = ALERT_REGISTERED)
  @Override
  public void accept(AlertRegistered command) {
    processes.stream() // parallel stream ?
        .filter(process -> process.supports(command))
        .forEach(process -> process.extractAndLoad(command));
  }
}
