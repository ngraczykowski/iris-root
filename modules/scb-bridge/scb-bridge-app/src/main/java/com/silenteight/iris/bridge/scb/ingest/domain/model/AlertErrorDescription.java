/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertErrorDescription {
  NO_MATCHES("Failed to extract alerts and matches. Matches are missing"),
  NONE("");
  @Getter
  private final String description;
}
