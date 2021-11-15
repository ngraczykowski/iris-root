package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
@ToString
public class AlertRejectedEvent extends DomainEvent {

  public static final String CHANNEL = "alertRejectedEventChannel";

  private final UUID alertId;
  private final String status;

}
