package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

/**
 * Alert has been stored.
 */
@RequiredArgsConstructor
@Getter
@ToString
public class AlertStoredEvent extends DomainEvent implements AlertId {

  private final UUID alertId;
}
