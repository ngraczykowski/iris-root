package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertDeliveredEvent;
import com.silenteight.payments.bridge.event.AlertUndeliveredEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexResponseDeliveredUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.UUID;

import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.DELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.UNDELIVERED;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class DeliveryStatusUpdateEndpoint {

  private final AlertMessageStatusUseCase alertMessageStatusUseCase;
  private final AlertMessageUseCase alertMessageUseCase;
  private final IndexResponseDeliveredUseCase indexResponseDeliveredUseCase;

  @ServiceActivator(inputChannel = AlertDeliveredEvent.CHANNEL)
  void apply(AlertDeliveredEvent event) {
    apply(event.getAlertId(), AlertMessageStatus.valueOf(event.getStatus()), DELIVERED);
  }

  @ServiceActivator(inputChannel = AlertUndeliveredEvent.CHANNEL)
  void apply(AlertUndeliveredEvent event) {
    apply(event.getAlertId(), AlertMessageStatus.valueOf(event.getStatus()), UNDELIVERED);
  }

  private void apply(UUID alertId, AlertMessageStatus status, DeliveryStatus deliveryStatus) {
    alertMessageStatusUseCase.transitionAlertMessageStatus(alertId, status, deliveryStatus);
    var alertData = alertMessageUseCase.findByAlertMessageId(alertId);
    indexResponseDeliveredUseCase.index(alertData, status.name(), deliveryStatus.name());
  }

}
