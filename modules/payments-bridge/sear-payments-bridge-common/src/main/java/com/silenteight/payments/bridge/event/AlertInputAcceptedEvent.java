package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The alert input data was delivered to the Universal Data Source.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
public class AlertInputAcceptedEvent extends DomainEvent {

  private final String alertId;

}
