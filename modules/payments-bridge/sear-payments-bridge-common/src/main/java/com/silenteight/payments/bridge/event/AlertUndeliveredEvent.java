package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

/**
 * Attempts to deliver feedback to the requesting part have failed. The further processing
 * has been stopped.
 */
@RequiredArgsConstructor
@Getter
@ToString
public class AlertUndeliveredEvent extends DomainEvent {

  private final UUID alertId;
  private final String status;

}
