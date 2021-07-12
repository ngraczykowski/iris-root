package com.silenteight.hsbc.bridge.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AlertStatus {

  STORED(false),
  PRE_PROCESSED(true),
  PROCESSING(false),
  RECOMMENDATION_READY(true),
  ERROR(false),
  COMPLETED(false);

  @Getter
  private boolean internal;
}
