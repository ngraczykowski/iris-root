package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ModelChangeStatus {
  SUCCESS("SUCCESS"),
  FAILURE("FAILURE");

  private final String name;
}
