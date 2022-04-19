package com.silenteight.adjudication.engine.solving.domain.event;

import lombok.Getter;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.DomainEvent;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Instant;
import java.util.UUID;

@Getter
abstract class DomainBase implements DomainEvent {

  protected String eventType;
  protected Instant occurredOn;
  protected UUID id;
  protected Long alertSolvingId;
  private String payload;

  DomainBase(
      final AlertSolving alertSolving, final String eventType) {
    this.payload = this.serialize(alertSolving);
    this.eventType = eventType;
    this.occurredOn = Instant.now();
    this.id = UUID.randomUUID();
    this.alertSolvingId = alertSolving.id();
  }

  DomainBase() {

  }

  @Override
  public Long alertSolvingId() {
    return this.alertSolvingId;
  }

  @Override
  public String eventType() {
    return this.eventType;
  }

  @Override
  public UUID id() {
    return this.id;
  }

  @Override
  public Instant occurredOn() {
    return this.occurredOn;
  }

  protected String serialize(final AlertSolving alertSolving) {
    if (null == alertSolving) {
      return null;
    }
    try {
      return OBJECT_MAPPER.writeValueAsString(alertSolving);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
