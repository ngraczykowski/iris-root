package com.silenteight.adjudication.engine.solving.domain;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {

  Instant occurredOn();

  UUID id();

  UUID alertSolvingId();

  String payload();
}
