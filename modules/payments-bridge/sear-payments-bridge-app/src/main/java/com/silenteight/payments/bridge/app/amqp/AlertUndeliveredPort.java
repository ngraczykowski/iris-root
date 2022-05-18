package com.silenteight.payments.bridge.app.amqp;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import java.util.UUID;

public interface AlertUndeliveredPort {

  void sendUndelivered(UUID alertId, AlertMessageStatus status);

}
