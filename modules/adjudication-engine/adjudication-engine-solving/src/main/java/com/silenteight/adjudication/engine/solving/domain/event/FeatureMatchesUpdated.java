package com.silenteight.adjudication.engine.solving.domain.event;

import com.silenteight.adjudication.engine.solving.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class FeatureMatchesUpdated implements DomainEvent {

  private final Instant occurredOn;

  public FeatureMatchesUpdated() {
    this.occurredOn = Instant.now();
  }

  @Override
  public Instant occurredOn() {
    return this.occurredOn;
  }

  @Override
  public UUID id() {
    return null;
  }

  @Override
  public UUID alertSolvingId() {
    return null;
  }

  @Override
  public String payload() {
    return null;
  }
}
