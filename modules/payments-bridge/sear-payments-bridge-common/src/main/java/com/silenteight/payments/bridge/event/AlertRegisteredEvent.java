package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;

/**
 * The alert was registered within Adjudication Engine.
 */
@Getter
@EqualsAndHashCode(of = {"alertId", "alertRegisteredName"}, callSuper = false)
public class AlertRegisteredEvent extends DomainEvent implements AlertDataIdentifier,
    AlertDtoIdentifier {

  private final String alertId;
  private final String alertRegisteredName;
  private final Map<String, String> matches;

  public AlertRegisteredEvent(String alertId, String registeredName, Map<String, String> matches) {
    this.alertId = alertId;
    this.alertRegisteredName = registeredName;
    this.matches = CollectionUtils.isEmpty(matches) ?
                   Collections.emptyMap() : Collections.unmodifiableMap(matches);
  }

}
