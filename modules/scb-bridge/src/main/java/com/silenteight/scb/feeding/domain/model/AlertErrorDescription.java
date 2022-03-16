package com.silenteight.scb.feeding.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertErrorDescription {
  CREATE_FEATURE_INPUT("Failed to create feature inputs."),
  NONE("");
  @Getter
  private final String description;
}
