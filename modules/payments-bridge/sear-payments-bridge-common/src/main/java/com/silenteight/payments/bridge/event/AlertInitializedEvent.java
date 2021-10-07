package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

/**
 * The alert processing has been initialized.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
public class AlertInitializedEvent extends DomainEvent
    implements AlertDataIdentifier, AlertDtoIdentifier {

  private final String alertId;

}
