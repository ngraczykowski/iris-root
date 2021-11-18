package com.silenteight.payments.bridge.app.integration;


import com.silenteight.payments.bridge.event.AlertUndeliveredEvent;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = AlertUndeliveredEvent.CHANNEL)
public interface AlertUndeliveredGateway {

  void send(AlertUndeliveredEvent event);

}
