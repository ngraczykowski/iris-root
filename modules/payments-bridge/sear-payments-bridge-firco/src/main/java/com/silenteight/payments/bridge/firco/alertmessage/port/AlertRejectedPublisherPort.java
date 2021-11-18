package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.event.AlertRejectedEvent;

public interface AlertRejectedPublisherPort {

  void send(AlertRejectedEvent event);

}
