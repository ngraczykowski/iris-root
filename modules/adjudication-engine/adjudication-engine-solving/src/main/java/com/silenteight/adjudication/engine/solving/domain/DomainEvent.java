package com.silenteight.adjudication.engine.solving.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public interface DomainEvent extends TraceEvent {

  ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

  // JSON AlertSolving
  String getPayload();
}
