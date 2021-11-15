package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

/**
 * The alert processing has been initialized.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
@ToString
public class AlertInitializedEvent extends DomainEvent {

  public static final String CHANNEL = "alertInitializedEventChannel";

  private final UUID alertId;

}
