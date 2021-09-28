package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The alert input data was delivered to the Universal Data Source.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId")
public class AlertInputAccepted implements DomainEvent {

  private final String alertId;

}
