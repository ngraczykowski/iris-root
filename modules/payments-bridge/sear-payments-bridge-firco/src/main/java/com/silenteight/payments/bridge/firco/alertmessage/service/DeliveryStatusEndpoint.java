package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.AlertDeliveredEvent;
import com.silenteight.payments.bridge.event.AlertUndeliveredEvent;
import com.silenteight.payments.bridge.event.DomainEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseResponseDelivery;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_DELIVERED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_UNDELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.DELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.UNDELIVERED;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
class DeliveryStatusEndpoint {

  private final AlertMessageStatusService alertMessageStatusService;
  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final IndexAlertUseCase indexAlertUseCase;

  @ServiceActivator(inputChannel = ALERT_UNDELIVERED)
  void apply(AlertUndeliveredEvent event) {
    alertMessageStatusService.transitionAlertMessageStatus(
        event.getAlertId(), AlertMessageStatus.valueOf(event.getStatus()), UNDELIVERED);
    indexAlert(event, event.getStatus(), UNDELIVERED);
  }

  @ServiceActivator(inputChannel = ALERT_DELIVERED)
  void apply(AlertDeliveredEvent event) {
    alertMessageStatusService.transitionAlertMessageStatus(
        event.getAlertId(), AlertMessageStatus.valueOf(event.getStatus()), DELIVERED);
    indexAlert(event, event.getStatus(), DELIVERED);
  }

  private void indexAlert(DomainEvent event, String status, DeliveryStatus deliveryStatus) {
    var alertData = event.getData(AlertData.class);
    var alert = alertBuilderFactory
        .newBuilder()
        .setDiscriminator(alertData.getDiscriminator())
        .addPayload(WarehouseResponseDelivery.builder()
            .status(status)
            .deliveryStatus(deliveryStatus.name())
            .build())
        .build();
    indexAlertUseCase.index(alert);
  }

}
