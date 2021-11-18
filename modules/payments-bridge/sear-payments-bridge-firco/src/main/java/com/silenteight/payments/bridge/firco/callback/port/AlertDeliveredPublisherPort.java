package com.silenteight.payments.bridge.firco.callback.port;

import com.silenteight.payments.bridge.event.AlertDeliveredEvent;

public interface AlertDeliveredPublisherPort {

  void send(AlertDeliveredEvent event);

}
