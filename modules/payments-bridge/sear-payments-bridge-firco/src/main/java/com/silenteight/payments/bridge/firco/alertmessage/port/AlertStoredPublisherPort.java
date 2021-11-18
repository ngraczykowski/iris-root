package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.event.AlertStoredEvent;

public interface AlertStoredPublisherPort {

  void send(AlertStoredEvent event);

}
