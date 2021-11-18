package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.RecommendationCompletedPublisherPort;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.firco.datasource.port.EtlUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
class EtlService implements EtlUseCase {

  private final List<EtlProcess> processes;
  private final ExtractAlertEtlResponseUseCase extractAlertEtlResponseUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final RecommendationCompletedPublisherPort recommendationCompletedPublisherPort;

  @LogContext
  @Override
  public void process(AlertRegisteredEvent command) {
    var alertId = command.getAeAlert().getAlertId();
    var alertName = command.getAeAlert().getAlertName();
    MDC.put("alertId", alertId.toString());
    MDC.put("alertName", alertName);

    try {
      var alertEtlResponse = getAlertEtlResponse(command);
      processes.stream()
          .filter(process -> process.supports(command))
          .forEach(process -> process.extractAndLoad(command, alertEtlResponse));

    } catch (UnsupportedMessageException exception) {
      log.error("Failed to process a message payload associated with the alert: {}. "
          + "Reject the message as DAMAGED", alertId, exception);
      recommendationCompletedPublisherPort.send(RecommendationCompletedEvent.fromBridge(alertId,
          AlertMessageStatus.REJECTED_DAMAGED.name(),
          RecommendationReason.DAMAGED.name()));
      throw exception;
    }
  }

  private AlertEtlResponse getAlertEtlResponse(AlertRegisteredEvent command) {
    var alertId = command.getAeAlert().getAlertId();
    var alertMessageDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);

    MDC.put("systemId", alertMessageDto.getSystemID());

    return extractAlertEtlResponseUseCase.createAlertEtlResponse(alertMessageDto);
  }

}
