package com.silenteight.adjudication.engine.solving.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.engine.solving.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
abstract class DomainBase implements DomainEvent {

  protected String eventType;
  protected Instant occurredOn;
  protected UUID id;
  protected Long alertSolvingId;

  @Override
  public String eventType() {
    return this.eventType;
  }

  @Override
  public Instant occurredOn() {
    return this.occurredOn;
  }

  @Override
  public UUID id() {
    return this.id;
  }

  @Override
  public Long alertSolvingId() {
    return this.alertSolvingId;
  }
}
