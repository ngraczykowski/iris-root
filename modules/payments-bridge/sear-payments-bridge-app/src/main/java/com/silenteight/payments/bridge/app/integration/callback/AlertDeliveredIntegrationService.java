package com.silenteight.payments.bridge.app.integration.callback;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.app.amqp.AlertUndeliveredPort;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.callback.port.AlertDeliveredPublisherPort;
import com.silenteight.payments.bridge.warehouse.index.port.IndexResponseDeliveredUseCase;

import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.DELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.UNDELIVERED;

@Component
@RequiredArgsConstructor
public class AlertDeliveredIntegrationService implements AlertDeliveredPublisherPort,
    AlertUndeliveredPort {

  private final AlertMessageStatusUseCase alertMessageStatusUseCase;
  private final AlertMessageUseCase alertMessageUseCase;
  private final IndexResponseDeliveredUseCase indexResponseDeliveredUseCase;

  @Override
  public void sendDelivered(UUID alertId, AlertMessageStatus status) {
    apply(alertId, status, DELIVERED);
  }

  @Override
  public void sendUndelivered(UUID alertId, AlertMessageStatus status) {
    apply(alertId, status, UNDELIVERED);
  }

  private void apply(UUID alertId, AlertMessageStatus status, DeliveryStatus deliveryStatus) {
    alertMessageStatusUseCase.transitionAlertMessageStatus(alertId, status, deliveryStatus);
    var alertData = alertMessageUseCase.findByAlertMessageId(alertId);
    indexResponseDeliveredUseCase.index(alertData, status.name(), deliveryStatus.name());
  }

}
