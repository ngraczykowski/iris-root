/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertErrorDescription {
  CREATE_FEATURE_INPUT("Failed to create feature inputs."),
  NONE("");
  @Getter
  private final String description;
}
