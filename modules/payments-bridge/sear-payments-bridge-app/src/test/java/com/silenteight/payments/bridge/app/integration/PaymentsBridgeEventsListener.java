package com.silenteight.payments.bridge.app.integration;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.common.event.LearningAlertRegisteredEvent;
import com.silenteight.payments.bridge.common.event.SolvingAlertRegisteredEvent;
import com.silenteight.payments.bridge.warehouse.index.model.WarehouseIndexRequestedEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Component
class PaymentsBridgeEventsListener {

  private final Set<SolvingAlertRegisteredEvent> solvingAlertRegisteredEvents = new HashSet<>();

  private final Set<LearningAlertRegisteredEvent> learningAlertRegisteredEvents = new HashSet<>();

  private final Set<ProductionDataIndexRequest> indexedAlerts = new HashSet<>();

  @EventListener
  public void onSolvingAlertRegisteredEvent(SolvingAlertRegisteredEvent event) {
    solvingAlertRegisteredEvents.add(event);
  }

  @EventListener
  public void onLearningAlertRegisteredEvent(LearningAlertRegisteredEvent event) {
    learningAlertRegisteredEvents.add(event);
  }

  @Bean
  public IntegrationFlow warehouseIndexedAlert() {
    return from("warehouseOutboundChannel")
        .transform(WarehouseIndexRequestedEvent.class, WarehouseIndexRequestedEvent::getRequest)
        .handle(ProductionDataIndexRequest.class, (payload, headers) -> {
          indexedAlerts.add(payload);
          return payload;
        })
        .get();
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

  boolean containsIndexedDiscriminator(String discriminator) {
    return indexedAlerts
        .stream()
        .anyMatch(
            ia -> containsDiscriminator(ia, discriminator)
        );
  }

  private static boolean containsDiscriminator(
      ProductionDataIndexRequest request, String discriminator) {
    return request.getAlertsList()
        .stream()
        .anyMatch(a -> a.getDiscriminator().equals(discriminator));
  }
}
