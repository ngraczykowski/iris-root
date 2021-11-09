package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.AlertInputAcceptedEvent;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;

import java.util.List;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_INPUT_ACCEPTED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED;

@RequiredArgsConstructor
@MessageEndpoint
@Slf4j
class EtlService {

  private final List<EtlProcess> processes;
  private final ExtractAlertEtlResponseUseCase extractAlertEtlResponseUseCase;
  private final CommonChannels commonChannels;

  @ServiceActivator(inputChannel = ALERT_REGISTERED, outputChannel = ALERT_INPUT_ACCEPTED)
  @LogContext
  AlertInputAcceptedEvent accept(AlertRegisteredEvent command) {
    MDC.put("alertId", command.getAlertId().toString());
    MDC.put("alertName", command.getAlertRegisteredName());

    process(command);

    return new AlertInputAcceptedEvent(
        command.getAlertId(), command.getAlertRegisteredName(), command.getMatches());
  }

  private void process(AlertRegisteredEvent command) {
    try {
      var alertEtlResponse = getAlertEtlResponse(command);
      processes.stream()
          .filter(process -> process.supports(command))
          .forEach(process -> process.extractAndLoad(command, alertEtlResponse));

    } catch (UnsupportedMessageException exception) {
      log.error("Failed to process a message payload associated with the alert: {}. "
          + "Reject the message as DAMAGED", command.getAlertId(), exception);
      commonChannels.recommendationCompleted().send(
          MessageBuilder.withPayload(
              RecommendationCompletedEvent.fromBridge(
                  command.getAlertId(),
                  AlertMessageStatus.REJECTED_DAMAGED.name(),
                  RecommendationReason.DAMAGED.name())
          ).build());
      throw exception;
    }
  }

  private AlertEtlResponse getAlertEtlResponse(AlertRegisteredEvent command) {
    var alertMessageDto = command.getData(AlertMessageDto.class);

    MDC.put("systemId", alertMessageDto.getSystemID());

    return extractAlertEtlResponseUseCase.createAlertEtlResponse(alertMessageDto);
  }

}
