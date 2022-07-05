package com.silenteight.payments.bridge.app.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.LearningAlertRegisteredEvent;
import com.silenteight.payments.bridge.common.event.SolvingAlertRegisteredEvent;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;

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

  private final Set<ResponseCompleted> responseCompletedEvents = new HashSet<>();

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

  @EventListener
  public void onResponseCompleted(ResponseCompleted responseCompleted) {
    log.info(
        "Received response alert event alertName:{} status:{}, recommendation:{}",
        responseCompleted.getAlert(),
        responseCompleted.getStatus(),
        responseCompleted.getRecommendation());
    responseCompletedEvents.add(responseCompleted);
  }

  boolean containsRegisteredAlert(UUID alertId) {
    return solvingAlertRegisteredEvents.stream()
        .anyMatch(are -> are.getAeAlert().getAlertId().equals(alertId));
  }

  boolean containsLearningRegisteredId(String alertId) {
    return learningAlertRegisteredEvents
        .stream()
        .anyMatch(are -> are.getAlertMessageId().equals(alertId));
  }

  boolean containsResponseCompleted(UUID alertId, String status) {
    return responseCompletedEvents.stream()
        .anyMatch(response -> responseCompletedContains(alertId, status, response));
  }

  long responseCount(UUID alertId, String status) {
    return responseCompletedEvents.stream()
        .filter(response -> responseCompletedContains(alertId, status, response))
        .count();
  }

  private static boolean responseCompletedContains(
      UUID alertId, String status, ResponseCompleted responseCompleted) {
    return responseCompleted.getAlert().contains(alertId.toString())
        && responseCompleted.getStatus().contains(status);
  }
}
