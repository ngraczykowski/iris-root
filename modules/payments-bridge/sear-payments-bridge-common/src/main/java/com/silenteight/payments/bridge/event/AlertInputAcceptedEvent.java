package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.silenteight.payments.bridge.common.model.AlertId;

import java.util.UUID;

/**
 * The alert input data was delivered to the Universal Data Source.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = "alertId", callSuper = false)
@ToString
public class AlertInputAcceptedEvent extends DomainEvent implements AlertId {

  private final UUID alertId;

}
