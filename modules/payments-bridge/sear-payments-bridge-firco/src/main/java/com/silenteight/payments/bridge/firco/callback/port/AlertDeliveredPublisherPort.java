package com.silenteight.payments.bridge.firco.callback.port;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import java.util.UUID;

public interface AlertDeliveredPublisherPort {

  void sendDelivered(UUID alertId, AlertMessageStatus status);

}
