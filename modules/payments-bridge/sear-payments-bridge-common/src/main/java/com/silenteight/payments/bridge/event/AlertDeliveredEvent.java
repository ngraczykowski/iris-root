package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class AlertDeliveredEvent extends DomainEvent implements AlertDataIdentifier {

  private final UUID alertId;
  private final String status;

}
