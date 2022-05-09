package com.silenteight.adjudication.engine.trace.listener;

import lombok.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class TraceMessage {

  @JsonProperty(required = true) String payload;
  @JsonProperty(required = true) String eventType;
  @JsonProperty(required = true) Instant occurredOn;
  @JsonProperty(required = true) UUID id;
  @JsonProperty(required = true) Long alertSolvingId;
}
