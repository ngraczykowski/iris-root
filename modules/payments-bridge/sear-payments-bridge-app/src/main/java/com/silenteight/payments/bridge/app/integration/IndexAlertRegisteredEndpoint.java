package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertAddedToAnalysisEvent;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertRegisteredUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class IndexAlertRegisteredEndpoint {

  private final AlertMessageStatusUseCase alertMessageStatusUseCase;
  private final AlertMessageUseCase alertMessageUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final IndexAlertRegisteredUseCase indexAlertRegisteredUseCase;

  @ServiceActivator(inputChannel = AlertAddedToAnalysisEvent.CHANNEL)
  void accept(AlertAddedToAnalysisEvent event) {
    var alertId = event.getAlertId();
    var alertData = alertMessageUseCase.findByAlertMessageId(alertId);
    var alertMessageDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);
    var status = alertMessageStatusUseCase.getStatus(alertId);

    indexAlertRegisteredUseCase.index(alertData, alertMessageDto,
        event.getAeAlert(), status.name());
  }
}
