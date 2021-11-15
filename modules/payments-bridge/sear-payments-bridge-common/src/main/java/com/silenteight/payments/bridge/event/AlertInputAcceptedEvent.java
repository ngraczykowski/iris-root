package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.common.model.AeAlert;

/**
 * The alert input data was delivered to the Universal Data Source.
 */
@RequiredArgsConstructor
@Getter
@ToString
public class AlertInputAcceptedEvent extends DomainEvent {

  public static final String CHANNEL = "alertInputAcceptedEventChannel";

  private final AeAlert aeAlert;

}
