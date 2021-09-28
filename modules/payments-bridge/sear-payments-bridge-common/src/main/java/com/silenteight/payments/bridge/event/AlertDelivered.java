package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

import java.util.Optional;

/**
 * The alert was delivered to the inbound channel from the persistent store (rabbitMQ).
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId")
public class AlertDelivered implements DomainEvent {

  private final String alertId;
  private AlertMessageModel alertModel;

  public AlertDelivered withAlertModel(AlertMessageModel model) {
    this.alertModel = model;
    return this;
  }

  public Optional<AlertMessageModel> getAlertModel() {
    return Optional.ofNullable(alertModel);
  }

}
