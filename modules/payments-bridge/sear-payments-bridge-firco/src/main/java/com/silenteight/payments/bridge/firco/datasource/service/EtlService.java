package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_INPUT_ACCEPTED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED;

@RequiredArgsConstructor
@MessageEndpoint
@Slf4j
class EtlService {

  private final List<EtlProcess> processes;
  private final ExtractAlertEtlResponseUseCase extractAlertEtlResponseUseCase;

  @ServiceActivator(inputChannel = ALERT_REGISTERED, outputChannel = ALERT_INPUT_ACCEPTED)
  AlertInputAcceptedEvent accept(AlertRegisteredEvent command) {
    processes.stream() // parallel stream ?
        .filter(process -> process.supports(command))
        .forEach(process -> process.extractAndLoad(command, getAlertEtlResponse(command)));

    return new AlertInputAcceptedEvent(command.getAlertId(), command.getAlertRegisteredName());
  }

  private AlertEtlResponse getAlertEtlResponse(AlertRegisteredEvent command) {
    return extractAlertEtlResponseUseCase.createAlertEtlResponse(
        command.getData(AlertMessageDto.class));
  }

}
