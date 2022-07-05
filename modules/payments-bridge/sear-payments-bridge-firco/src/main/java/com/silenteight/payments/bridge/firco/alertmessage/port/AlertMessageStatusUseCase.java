package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;

import java.util.UUID;

public interface AlertMessageStatusUseCase {

  void transitionAlertDeliveryStatus(UUID alertMessageId, DeliveryStatus delivery);

  boolean transitionAlertMessageStatus(
      UUID alertMessageId, AlertMessageStatus destinationStatus, DeliveryStatus delivery);

  AlertMessageStatus getStatus(UUID alertMessageId);
}
