package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
public class AlertRejected extends DomainEvent implements AlertDtoIdentifier, AlertDataIdentifier {

  public static final String REJECTION_REASON_HEADER = "rejection-reason";
  public static final String REASON_SKIP = "SKIP";

  private final String alertId;
  private final String status;

}
