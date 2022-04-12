package com.silenteight.adjudication.engine.solving.domain.event;

import com.silenteight.adjudication.engine.solving.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class MatchesUpdated implements DomainEvent {

  @Override
  public Instant occurredOn() {
    return null;
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
