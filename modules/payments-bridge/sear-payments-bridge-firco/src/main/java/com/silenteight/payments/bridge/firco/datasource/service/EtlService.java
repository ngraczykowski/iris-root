package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
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
  @LogContext
  AlertInputAcceptedEvent accept(AlertRegisteredEvent command) {
    MDC.put("alertId", command.getAlertId().toString());
    MDC.put("alertName", command.getAlertRegisteredName());

    var alertEtlResponse = getAlertEtlResponse(command);

    processes.stream() // parallel stream ?
        .filter(process -> process.supports(command))
        .forEach(process -> process.extractAndLoad(command, alertEtlResponse));

    return new AlertInputAcceptedEvent(command.getAlertId(), command.getAlertRegisteredName());
  }

  private AlertEtlResponse getAlertEtlResponse(AlertRegisteredEvent command) {
    var alertMessageDto = command.getData(AlertMessageDto.class);

    MDC.put("systemId", alertMessageDto.getSystemID());

    return extractAlertEtlResponseUseCase.createAlertEtlResponse(alertMessageDto);
  }

}
