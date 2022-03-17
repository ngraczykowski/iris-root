package com.silenteight.scb.ingest.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertErrorDescription {
  NO_MATCHES("Failed to extract alerts and matches. Matches are missing"),
  NONE("");
  @Getter
  private final String description;
}
