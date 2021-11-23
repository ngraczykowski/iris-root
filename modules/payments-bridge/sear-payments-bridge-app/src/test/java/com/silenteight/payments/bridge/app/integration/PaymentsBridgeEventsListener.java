package com.silenteight.payments.bridge.app.integration;

import com.silenteight.payments.bridge.common.event.AlertRegisteredEvent;

import com.google.common.collect.Sets;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
class PaymentsBridgeEventsListener {

  private final Set<AlertRegisteredEvent> alertRegisteredEvents = Sets.newConcurrentHashSet();

  @EventListener
  public void onAlertRegisteredEvent(AlertRegisteredEvent event) {
    alertRegisteredEvents.add(event);
  }

  boolean containsRegisteredAlert(UUID alertId) {
    return alertRegisteredEvents.stream()
        .anyMatch(are -> are.getAeAlert().getAlertId().equals(alertId));
  }

}
