package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

/**
 * The alert was delivered to the inbound channel from the persistent store (rabbitMQ).
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
public class RecommendationReceived extends DomainEvent
    implements AlertDataIdentifier, AlertDtoIdentifier {

  private final String alertId;

}
