package com.silenteight.payments.bridge.app.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.LearningAlertRegisteredEvent;
import com.silenteight.payments.bridge.common.event.SolvingAlertRegisteredEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
class PaymentsBridgeEventsListener {

  private final Set<SolvingAlertRegisteredEvent> solvingAlertRegisteredEvents = new HashSet<>();

  private final Set<LearningAlertRegisteredEvent> learningAlertRegisteredEvents = new HashSet<>();

  @EventListener
  public void onSolvingAlertRegisteredEvent(SolvingAlertRegisteredEvent event) {
    log.info("Received solving alert event alertId:{} alertName:{}",
        event.getAeAlert().getAlertId(),
        event.getAeAlert().getAlertName());
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

  boolean containsLearningRegisteredSystemId(String alertId) {
    return learningAlertRegisteredEvents
        .stream()
        .anyMatch(are -> are.getAlertMessageId().equals(alertId));
  }
}
