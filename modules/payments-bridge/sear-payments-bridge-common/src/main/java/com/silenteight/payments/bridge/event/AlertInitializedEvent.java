package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

import java.util.UUID;

/**
 * The alert processing has been initialized.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
@ToString
public class AlertInitializedEvent extends DomainEvent
    implements AlertDataIdentifier, AlertDtoIdentifier {

  private final UUID alertId;

}
