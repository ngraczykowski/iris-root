package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.common.model.AeAlert;

/**
 * The alert was registered within Adjudication Engine.
 */
@Getter
@ToString
@RequiredArgsConstructor
public class AlertRegisteredEvent extends DomainEvent {

  public static final String CHANNEL = "alertRegisteredEventChannel";

  private final AeAlert aeAlert;

}
