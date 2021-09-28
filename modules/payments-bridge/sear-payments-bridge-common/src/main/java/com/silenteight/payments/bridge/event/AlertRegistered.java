package com.silenteight.payments.bridge.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * The alert was registered within Adjudication Engine.
 */
@Getter
@EqualsAndHashCode(of = {"alertId", "alertRegisteredName"})
public class AlertRegistered implements DomainEvent {

  private final String alertId;
  private final String alertRegisteredName;
  private final Map<String, String> matches;

  private AlertMessageModel alertModel;
  private ObjectNode originalMessage;

  public AlertRegistered(String alertId, String alertRegisteredName, Map<String, String> matches) {
    this.alertId = alertId;
    this.alertRegisteredName = alertRegisteredName;
    this.matches = CollectionUtils.isEmpty(matches) ?
                   Collections.emptyMap() : Collections.unmodifiableMap(matches);
  }

  public AlertRegistered withAlertModel(AlertMessageModel model) {
    this.alertModel = model;
    return this;
  }

  public AlertRegistered withOriginalMessage(ObjectNode payload) {
    this.originalMessage = payload;
    return this;
  }

  public Optional<AlertMessageModel> getAlertModel() {
    return Optional.ofNullable(alertModel);
  }

  public Optional<ObjectNode> getOriginalMessage() {
    return Optional.ofNullable(originalMessage);
  }

}
