package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
public class AlertAddedToAnalysisEvent extends DomainEvent implements AlertId {

  public static final String CHANNEL = "alertAddedToAnalysisEventChannel";

  private final AeAlert aeAlert;

  @Override
  public UUID getAlertId() {
    return aeAlert.getAlertId();
  }
}
