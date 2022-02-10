package com.silenteight.payments.bridge.app.integration;

import com.silenteight.payments.bridge.common.event.LearningAlertRegisteredEvent;
import com.silenteight.payments.bridge.common.event.SolvingAlertRegisteredEvent;

import com.google.common.collect.Sets;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
class PaymentsBridgeEventsListener {

  private final Set<SolvingAlertRegisteredEvent> solvingAlertRegisteredEvents =
      Sets.newConcurrentHashSet();

  private final Set<LearningAlertRegisteredEvent> learningAlertRegisteredEvents =
      Sets.newConcurrentHashSet();

  @EventListener
  public void onSolvingAlertRegisteredEvent(SolvingAlertRegisteredEvent event) {
    solvingAlertRegisteredEvents.add(event);
  }

  @EventListener
  public void onLearningAlertRegisteredEvent(LearningAlertRegisteredEvent event) {
    learningAlertRegisteredEvents.add(event);
  }

  boolean containsRegisteredAlert(UUID alertId) {
    return solvingAlertRegisteredEvents.stream()
        .anyMatch(are -> are.getAeAlert().getAlertId().equals(alertId));
  }

  boolean containsLearningRegisteredSystemId(String systemId) {
    return learningAlertRegisteredEvents
        .stream()
        .anyMatch(are -> are.getSystemId().equals(systemId));
  }
}
