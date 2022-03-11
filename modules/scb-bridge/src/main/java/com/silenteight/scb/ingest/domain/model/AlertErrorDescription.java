package com.silenteight.scb.ingest.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertErrorDescription {
  EXTRACTION("Failed to extract alerts and matches."),
  NO_MATCHES("Failed to extract alerts and matches. Matches are missing"),
  FLATTEN("Failed to flatten alert payload."),
  GETTING_FROM_EXTERNAL_DB("Failed to get customer Alerts from the external db."),
  GETTING_FROM_DB("Failed to get raw alerts from the internal db."),
  MISSING("Alert was missing."),
  NONE("");
  @Getter
  private final String description;
}
