package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.common.model.AlertId;
import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
@ToString
public class AlertAddedToAnalysisEvent extends DomainEvent
    implements AlertId, AlertDataIdentifier {

  private final UUID alertId;
  private final String alertRegisteredName;

}
