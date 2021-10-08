package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
@ToString
public class AlertRejectedEvent extends DomainEvent
    implements AlertDtoIdentifier, AlertDataIdentifier {

  private final UUID alertId;
  private final String status;

}
