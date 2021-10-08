package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * The alert was registered within Adjudication Engine.
 */
@Getter
@EqualsAndHashCode(of = {"alertId", "alertRegisteredName"}, callSuper = false)
@ToString
public class AlertRegisteredEvent extends DomainEvent implements AlertDataIdentifier,
    AlertDtoIdentifier {

  private final UUID alertId;
  private final String alertRegisteredName;
  private final Map<String, String> matches;

  public AlertRegisteredEvent(UUID alertId, String registeredName, Map<String, String> matches) {
    this.alertId = alertId;
    this.alertRegisteredName = registeredName;
    this.matches = CollectionUtils.isEmpty(matches) ?
                   Collections.emptyMap() : Collections.unmodifiableMap(matches);
  }

}
