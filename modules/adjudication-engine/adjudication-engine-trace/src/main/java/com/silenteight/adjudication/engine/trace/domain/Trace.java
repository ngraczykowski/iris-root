package com.silenteight.adjudication.engine.trace.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Trace {

  private final UUID eventId;
  private final String eventType;
  private final String payload;
  private final String payloadHash;
  private final OffsetDateTime ocurrenceOn;
}
