package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.event.AlertRegistered;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.firco.datasource.port.EtlUseCase;
import com.silenteight.payments.bridge.svb.etl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED;

@RequiredArgsConstructor
@MessageEndpoint
@Slf4j
class EtlService implements EtlUseCase {

  private final List<EtlProcess> processes;
  private final ExtractAlertEtlResponseUseCase extractAlertEtlResponseUseCase;

  @ServiceActivator(inputChannel = ALERT_REGISTERED)
  @Override
  public void accept(AlertRegistered command) {
    processes.stream() // parallel stream ?
        .filter(process -> process.supports(command))
        .forEach(process -> process.extractAndLoad(command, getAlertEtlResponse(command)));
  }

  private AlertEtlResponse getAlertEtlResponse(AlertRegistered command) {
    return extractAlertEtlResponseUseCase.createAlertEtlResponse(
        command.getData(AlertMessageDto.class));
  }
}
